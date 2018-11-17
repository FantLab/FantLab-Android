package ru.fantlab.android.ui.modules.search.works

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.SearchWork
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class SearchWorksPresenter : BasePresenter<SearchWorksMvp.View>(), SearchWorksMvp.Presenter {

	private var works: ArrayList<SearchWork> = arrayListOf()
	private var page: Int = 1
	private var previousTotal: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE

	override fun onItemClick(position: Int, v: View?, item: SearchWork) {
		view?.onItemClicked(item)
	}

	override fun onItemLongClick(position: Int, v: View?, item: SearchWork) {
	}

	override fun getWorks(): ArrayList<SearchWork> = works

	override fun getCurrentPage(): Int = page

	override fun getPreviousTotal(): Int = previousTotal

	override fun setCurrentPage(page: Int) {
		this.page = page
	}

	override fun setPreviousTotal(previousTotal: Int) {
		this.previousTotal = previousTotal
	}

	override fun onCallApi(page: Int, parameter: String?): Boolean {
		if (page == 1) {
			lastPage = Integer.MAX_VALUE
			sendToView { it.getLoadMore().reset() }
		}
		setCurrentPage(page)
		if (page > lastPage || lastPage == 0 || parameter == null) {
			sendToView { it.hideProgress() }
			return false
		}
		if (previousTotal == 1000) {
			sendToView {
				it.hideProgress()
				it.showMessage(R.string.error, R.string.results_warning)
			}
			return false
		}
		makeRestCall(
				DataManager.searchWorks(parameter, page)
						.toObservable(),
				Consumer { response ->
					lastPage = response.works.last
					sendToView {
						it.onNotifyAdapter(response.works.items, page)
						it.onSetTabCount(response.works.totalCount)
					}
				}
		)
		return true
	}
}