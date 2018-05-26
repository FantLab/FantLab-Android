package ru.fantlab.android.ui.modules.author.responses

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface AuthorResponsesMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener {

		fun onNotifyAdapter(items: ArrayList<Response>, page: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onSetTabCount(count: Int)

		fun onItemClicked(item: Response)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Response>,
			BaseMvp.PaginationListener<Int> {

		fun getResponses(): ArrayList<Response>

		fun onWorkOffline(authorId: Int)
	}
}