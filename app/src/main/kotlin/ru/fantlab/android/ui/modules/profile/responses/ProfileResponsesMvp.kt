package ru.fantlab.android.ui.modules.profile.responses

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.provider.rest.ResponsesSortOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.viewholder.ResponseViewHolder
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface ProfileResponsesMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener,
			ContextMenuDialogView.ListDialogViewActionCallback,
			ResponseViewHolder.OnOpenContextMenu {

		fun onNotifyAdapter(items: ArrayList<Response>, page: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onSetTabCount(count: Int)

		fun onItemClicked(item: Response)

		fun onItemLongClicked(item: Response, position: Int)

		fun onSetVote(position: Int, votesCount: String)

		fun onResponseDelete(position: Int)

		override fun onOpenContextMenu(userItem: Response)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Response>,
			BaseMvp.PaginationListener<Int> {

		fun getResponses(userId: Int, force: Boolean)

		fun onSendVote(item: Response, position: Int, voteType: String)

		fun onDeleteResponse(workId: Int, commentId: Int, position: Int)

		fun setCurrentSort(sortValue: String)

		fun getCurrentSort(): ResponsesSortOption
	}
}