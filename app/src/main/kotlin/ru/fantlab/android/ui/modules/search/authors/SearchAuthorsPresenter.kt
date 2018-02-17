package ru.fantlab.android.ui.modules.search.authors

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.SearchAuthorModel
import ru.fantlab.android.provider.rest.RestProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter


class SearchAuthorsPresenter : BasePresenter<SearchAuthorsMvp.View>(), SearchAuthorsMvp.Presenter {

	private var authors: ArrayList<SearchAuthorModel> = ArrayList()
	private var page: Int = 0
	private var previousTotal: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE

	override fun onItemClick(position: Int, v: View?, item: SearchAuthorModel) {
		view?.onItemClicked(item)
	}

	override fun onItemLongClick(position: Int, v: View?, item: SearchAuthorModel?) {
	}

	override fun getAuthors(): ArrayList<SearchAuthorModel> = authors

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
			sendToView { view -> view.getLoadMore().reset() }
		}
		setCurrentPage(page)
		if (page > lastPage || lastPage == 0 || parameter == null) {
			sendToView { view -> view.hideProgress() }
			return false
		}
		makeRestCall(RestProvider.getSearchService().searchAuthors(parameter, page), Consumer { response ->
			run {
				lastPage = response.last
				sendToView { view ->
					run {
						view.onNotifyAdapter(response.items, page)
						if (!response.incompleteResults) {
							view.onSetTabCount(response.totalCount)
						} else {
							view.showMessage(R.string.error, R.string.results_warning)
						}
					}
				}
			}
		})
		return true
	}
}