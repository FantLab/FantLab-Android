package ru.fantlab.android.ui.modules.search.works

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.newmodel.SearchWork
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface SearchWorksMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: ArrayList<SearchWork>, page: Int)

		fun onSetTabCount(count: Int)

		fun onSetSearchQuery(query: String)

		fun onQueueSearch(query: String)

		fun getLoadMore(): OnLoadMore<String>

		fun onItemClicked(item: SearchWork)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<SearchWork>,
			BaseMvp.PaginationListener<String> {

		fun getWorks(): ArrayList<SearchWork>
	}
}