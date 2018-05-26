package ru.fantlab.android.provider.rest.loadmore

import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.scroll.InfiniteScroll

class OnLoadMore<P>(private val presenter: BaseMvp.PaginationListener<P>?, var parameter: P? = null)
	: InfiniteScroll() {

	override fun onLoadMore(page: Int, totalItemsCount: Int): Boolean {
		presenter?.let {
			presenter.setPreviousTotal(totalItemsCount)
			return presenter.onCallApi(page + 1, parameter)
		}
		return false
	}
}