package ru.fantlab.android.ui.modules.profile.bookcases

import android.view.View
import io.reactivex.Single
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.response.BookcasesResponse
import ru.fantlab.android.provider.rest.getUserBookcasesPath
import ru.fantlab.android.provider.storage.DbProvider

class ProfileBookcasesPresenter : BasePresenter<ProfileBookcasesMvp.View>(),
        ProfileBookcasesMvp.Presenter {

    private var page: Int = 1
    private var previousTotal: Int = 0
    private var lastPage: Int = Integer.MAX_VALUE

    override fun onCallApi(page: Int, parameter: Int?): Boolean {
        if (page == 1) {
            lastPage = Integer.MAX_VALUE
            sendToView { it.getLoadMore().reset() }
        }
        setCurrentPage(page)
        if (page > lastPage || lastPage == 0 || parameter == null) {
            sendToView { it.hideProgress() }
            return false
        }

        return true
    }

    override fun getBookcases(userId: Int, force: Boolean) {
        if (force) {
            lastPage = Integer.MAX_VALUE
            sendToView { it.getLoadMore().reset() }
            setCurrentPage(1)
        }
        makeRestCall(
                getBookcasesInternal(userId, force).toObservable(),
                Consumer { (bookcases, totalCount, lastPage) ->
                    this.lastPage = lastPage
                    sendToView {
                        with (it) {
                            onNotifyAdapter(bookcases, page)
                            onSetTabCount(totalCount)
                        }
                    }
                }
        )
    }

    private fun getBookcasesInternal(userId: Int, force: Boolean) =
            getBookcasesFromServer(userId)
                    .onErrorResumeNext { throwable ->
                        if (page == 1 && !force) {
                            getBookcasesFromDb(userId)
                        } else {
                            throw throwable
                        }
                    }

    private fun getBookcasesFromServer(userId: Int): Single<Triple<ArrayList<Bookcase>, Int, Int>> =
            DataManager.getUserBookcases(userId, page)
                    .map { getBookcases(it) }

    private fun getBookcasesFromDb(userId: Int): Single<Triple<ArrayList<Bookcase>, Int, Int>> =
            DbProvider.mainDatabase
                    .responseDao()
                    .get(getUserBookcasesPath(userId, 1))
                    .map { it.response }
                    .map { BookcasesResponse.Deserializer(perPage = 50).deserialize(it) }
                    .map { getBookcases(it) }

    private fun getBookcases(response: BookcasesResponse): Triple<ArrayList<Bookcase>, Int, Int> =
            Triple(response.responses.items, response.responses.totalCount, response.responses.last)

    override fun onItemClick(position: Int, v: View?, item: Bookcase) {
        sendToView { it.onItemClicked(item) }
    }

    override fun onItemLongClick(position: Int, v: View?, item: Bookcase) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrentPage(): Int = page

    override fun getPreviousTotal(): Int = previousTotal

    override fun setCurrentPage(page: Int) {
        this.page = page
    }

    override fun setPreviousTotal(previousTotal: Int) {
        this.previousTotal = previousTotal
    }

}