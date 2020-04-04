package ru.fantlab.android.ui.modules.forums.topic

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.viewholder.TopicMessagesViewHolder
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface TopicMvp {

	interface View : BaseMvp.View,
			android.view.View.OnClickListener,
			SwipeRefreshLayout.OnRefreshListener,
			TopicMessagesViewHolder.MessageMenu,
			ContextMenuDialogView.ListDialogViewActionCallback {

		fun onNotifyAdapter(items: ArrayList<ForumTopic.Message>, page: Int)

		fun getLoadMore(): OnLoadMore<Int>

		fun onItemClicked(item: ForumTopic.Message)

		fun onItemLongClicked(position: Int, v: android.view.View?, item: ForumTopic.Message)

		fun onMessageDeleted(messageId: Int)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseMvp.PaginationListener<Int>,
			BaseViewHolder.OnItemClickListener<ForumTopic.Message> {

		fun getMessages(force: Boolean)
	}
}