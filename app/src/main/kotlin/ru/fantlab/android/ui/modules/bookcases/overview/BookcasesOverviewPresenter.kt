package ru.fantlab.android.ui.modules.bookcases.overview

import android.view.View
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.data.dao.response.BookcasesResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getPersonalBookcasesPath
import ru.fantlab.android.provider.rest.getUserBookcasesPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class BookcasesOverviewPresenter : BasePresenter<BookcasesOverviewMvp.View>(),
        BookcasesOverviewMvp.Presenter {

    var isPrivateCase = false
    var userId = -1

    override fun getBookcases(userId: Int, isPrivateCase: Boolean, force: Boolean) {
        this.isPrivateCase = isPrivateCase
        this.userId = userId
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
            (if (isPrivateCase) DataManager.getPersonalBookcases() else DataManager.getUserBookcases(userId))
                    .map { getBookcases(it) }

    private fun getBookcasesFromDb(): Single<Optional<ArrayList<Bookcase>>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(if (isPrivateCase) getPersonalBookcasesPath() else getUserBookcasesPath(userId))
                    .map { it.response }
                    .map { BookcasesResponse.Deserializer().deserialize(it) }
                    .map { getBookcases(it) }

    private fun getBookcases(response: BookcasesResponse): Optional<ArrayList<Bookcase>> =
            response.items.toOptional()

    override fun onItemClick(position: Int, v: View?, item: Bookcase) {
        sendToView { it.onItemClicked(item, position) }
    }

    override fun onItemLongClick(position: Int, v: View?, item: Bookcase) {}
}