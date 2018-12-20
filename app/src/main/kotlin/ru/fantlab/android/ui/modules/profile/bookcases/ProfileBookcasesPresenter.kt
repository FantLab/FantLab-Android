package ru.fantlab.android.ui.modules.profile.bookcases

import android.view.View
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

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

    override fun onItemClick(position: Int, v: View?, item: Bookcase) {
        sendToView { it.onItemClicked(item) }
    }

    override fun onItemLongClick(position: Int, v: View?, item: Bookcase?) {
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