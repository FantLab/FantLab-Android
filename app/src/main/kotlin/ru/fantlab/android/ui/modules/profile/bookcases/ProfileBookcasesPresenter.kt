package ru.fantlab.android.ui.modules.profile.bookcases

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
import ru.fantlab.android.provider.rest.getUserBookcasesPath
import ru.fantlab.android.provider.storage.DbProvider

class ProfileBookcasesPresenter : BasePresenter<ProfileBookcasesMvp.View>(),
        ProfileBookcasesMvp.Presenter {

    override fun onFragmentCreated(bundle: Bundle) {
        val userId = bundle.getInt(BundleConstant.EXTRA)
        getBookcases(userId, false)
    }

    override fun getBookcases(userId: Int, force: Boolean) {
        makeRestCall(
                getBookcasesInternal(userId, force).toObservable(),
                Consumer { bookcases ->
                    sendToView { it.onInitViews(bookcases.toNullable()) }
                }
        )
    }

    private fun getBookcasesInternal(userId: Int, force: Boolean) =
            getBookcasesFromServer(userId)
                    .onErrorResumeNext { throwable ->
                        if (!force) {
                            getBookcasesFromDb(userId)
                        } else {
                            throw throwable
                        }
                    }

    private fun getBookcasesFromServer(userId: Int): Single<Optional<ArrayList<Bookcase>>> =
            DataManager.getUserBookcases(userId)
                    .map { getBookcases(it) }

    private fun getBookcasesFromDb(userId: Int): Single<Optional<ArrayList<Bookcase>>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getUserBookcasesPath(userId))
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