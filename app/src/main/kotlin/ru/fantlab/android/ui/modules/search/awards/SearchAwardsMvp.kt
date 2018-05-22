package ru.fantlab.android.ui.modules.search.awards

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.newmodel.SearchAward
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface SearchAwardsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: ArrayList<SearchAward>, page: Int)

		fun onSetTabCount(count: Int)

		fun onSetSearchQuery(query: String)

		fun onQueueSearch(query: String)

		fun getLoadMore(): OnLoadMore<String>

		fun onItemClicked(item: SearchAward)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<SearchAward>,
			BaseMvp.PaginationListener<String> {

		fun getAwards(): ArrayList<SearchAward>
	}
}