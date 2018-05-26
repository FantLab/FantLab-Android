package ru.fantlab.android.ui.modules.search.editions

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.newmodel.SearchEdition
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface SearchEditionsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: ArrayList<SearchEdition>, page: Int)

		fun onSetTabCount(count: Int)

		fun onSetSearchQuery(query: String)

		fun onQueueSearch(query: String, isIsbn: Boolean)

		fun getLoadMore(): OnLoadMore<String>

		fun onItemClicked(item: SearchEdition)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<SearchEdition>,
			BaseMvp.PaginationListener<String> {

		fun getEditions(): ArrayList<SearchEdition>
	}
}