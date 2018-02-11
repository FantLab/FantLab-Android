package ru.fantlab.android.ui.modules.search.authors

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.SearchAuthorsModel
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface SearchAuthorsMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: List<SearchAuthorsModel>?, page: Int)

		fun onSetTabCount(count: Int)

		fun onSetSearchQuery(query: String)

		fun onQueueSearch(query: String)

		fun getLoadMore(): OnLoadMore<String>

		fun onItemClicked(item: SearchAuthorsModel)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<SearchAuthorsModel>,
			BaseMvp.PaginationListener<String> {

		fun getAuthors() : ArrayList<SearchAuthorsModel>
	}
}