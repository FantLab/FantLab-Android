package ru.fantlab.android.ui.modules.work.responses

import android.support.v4.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.provider.rest.ResponsesSortOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.viewholder.WorkResponseViewHolder
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface WorkResponsesMvp {

	interface View : BaseMvp.View,
			SwipeRefreshLayout.OnRefreshListener,
			android.view.View.OnClickListener,
			ContextMenuDialogView.ListDialogViewActionCallback,
			WorkResponseViewHolder.OnOpenContextMenu {

		fun onNotifyAdapter(items: ArrayList<Response>, page: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onSetTabCount(count: Int)

		fun onItemClicked(item: Response)

		fun onItemLongClicked(position: Int, v: android.view.View?, item: Response)

		fun onSetVote(position: Int, votesCount: String)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Response>,
			BaseMvp.PaginationListener<Int> {

		fun getResponses(page: Int, force: Boolean)

		fun onSendVote(item: Response, position: Int, voteType: String)

		fun setCurrentSort(sortValue: String)

		fun getCurrentSort(): ResponsesSortOption
	}
}