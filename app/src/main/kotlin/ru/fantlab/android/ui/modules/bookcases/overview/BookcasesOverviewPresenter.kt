package ru.fantlab.android.ui.modules.bookcases.overview

import android.os.Bundle
import android.view.View
import io.reactivex.Single
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.response.BookcasesResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.getPersonalBookcasesPath
import ru.fantlab.android.provider.storage.DbProvider

class BookcasesOverviewPresenter : BasePresenter<BookcasesOverviewMvp.View>(),
        BookcasesOverviewMvp.Presenter {

    override fun onFragmentCreated() {
        getBookcases(false)
    }

    override fun getBookcases(force: Boolean) {
        makeRestCall(
                getBookcasesInternal(force).toObservable(),
                Consumer { bookcases ->
                    sendToView { it.onInitViews(bookcases.toNullable()) }
                }
        )
    }

    private fun getBookcasesInternal(force: Boolean) =
            getBookcasesFromServer()
                    .onErrorResumeNext { throwable ->
                        if (!force) {
                            getBookcasesFromDb()
                        } else {
                            throw throwable
                        }
                    }

    private fun getBookcasesFromServer(): Single<Optional<ArrayList<Bookcase>>> =
            DataManager.getPersonalBookcases()
                    .map { getBookcases(it) }

    private fun getBookcasesFromDb(): Single<Optional<ArrayList<Bookcase>>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getPersonalBookcasesPath())
                    .map { it.response }
                    .map { BookcasesResponse.Deserializer().deserialize(it) }
                    .map { getBookcases(it) }

    private fun getBookcases(response: BookcasesResponse): Optional<ArrayList<Bookcase>> =
            response.items.toOptional()

    override fun onItemClick(position: Int, v: View?, item: Bookcase) {
        sendToView { it.onItemClicked(item, position) }
    }

    override fun onItemLongClick(position: Int, v: View?, item: Bookcase) {
        TODO("not implemented")
    }
}