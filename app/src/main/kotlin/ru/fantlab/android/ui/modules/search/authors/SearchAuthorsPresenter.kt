package ru.fantlab.android.ui.modules.search.authors

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.SearchAuthor
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter


class SearchAuthorsPresenter : BasePresenter<SearchAuthorsMvp.View>(), SearchAuthorsMvp.Presenter {

	private var authors: ArrayList<SearchAuthor> = arrayListOf()
	private var page: Int = 1
	private var previousTotal: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE

	override fun onItemClick(position: Int, v: View?, item: SearchAuthor) {
		view?.onItemClicked(item)
	}

	override fun onItemLongClick(position: Int, v: View?, item: SearchAuthor) {
	}

	override fun getAuthors(): ArrayList<SearchAuthor> = authors

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
				DataManager.searchAuthors(parameter, page)
						.map { it.get() }
						.toObservable(),
				Consumer { response ->
					lastPage = response.authors.last
					sendToView {
						it.onNotifyAdapter(response.authors.items, page)
						it.onSetTabCount(response.authors.totalCount)
					}
				}
		)
		return true
	}
}