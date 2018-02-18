package ru.fantlab.android.ui.modules.search.editions

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.SearchEditionModel
import ru.fantlab.android.provider.rest.RestProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class SearchEditionsPresenter : BasePresenter<SearchEditionsMvp.View>(), SearchEditionsMvp.Presenter {

	private var editions: ArrayList<SearchEditionModel> = ArrayList()
	private var page: Int = 0
	private var previousTotal: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE

	override fun onItemClick(position: Int, v: View?, item: SearchEditionModel) {
		view?.onItemClicked(item)
	}

	override fun onItemLongClick(position: Int, v: View?, item: SearchEditionModel?) {
	}

	override fun getEditions(): ArrayList<SearchEditionModel> = editions

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
		makeRestCall(RestProvider.getSearchService().searchEditions(parameter, page), Consumer {
			lastPage = it.last
			sendToView { view ->
				view.onNotifyAdapter(it.items, page)
				if (!it.incompleteResults) {
					view.onSetTabCount(it.totalCount)
				} else {
					view.showMessage(R.string.error, R.string.results_warning)
				}
			}
		})
		return true
	}
}