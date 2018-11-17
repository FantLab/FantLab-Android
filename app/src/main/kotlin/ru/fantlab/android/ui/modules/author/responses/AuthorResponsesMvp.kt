package ru.fantlab.android.ui.modules.author.responses

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.viewholder.ResponseViewHolder
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.SortView
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.scroll.InfiniteScroll

interface AuthorResponsesMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener, ContextMenuDialogView.ListDialogViewActionCallback,
			ResponseViewHolder.OnOpenContextMenu,
			InfiniteScroll.OnScrollResumed,
			SortView.SortViewListener {

		fun onNotifyAdapter(items: ArrayList<Response>, page: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onSetTabCount(count: Int)

		fun onItemClicked(item: Response)

		fun onSetVote(votesCount: Int, position: Int)

		override fun onOpenContextMenu(userItem: Response)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Response>,
			BaseMvp.PaginationListener<Int> {

		fun setCurrentSort(sortValue: String)

		fun onSendVote(responseId: Int, voteType: String, position: Int)
	}
}