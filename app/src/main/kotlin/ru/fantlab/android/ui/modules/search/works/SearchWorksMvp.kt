package ru.fantlab.android.ui.modules.search.works

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.SearchWorkModel
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface SearchWorksMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: List<SearchWorkModel>?, page: Int)

		fun onSetTabCount(count: Int)

		fun onSetSearchQuery(query: String)

		fun onQueueSearch(query: String)

		fun getLoadMore(): OnLoadMore<String>

		fun onItemClicked(item: SearchWorkModel)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<SearchWorkModel>,
			BaseMvp.PaginationListener<String> {

		fun getWorks(): ArrayList<SearchWorkModel>
	}
}