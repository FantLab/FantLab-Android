package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.forum_topic_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Forum
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder


class ForumTopicsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Forum.Topic, ForumTopicsViewHolder>)
	: BaseViewHolder<Forum.Topic>(itemView, adapter) {

	override fun bind(topic: Forum.Topic) {
		itemView.topicTitle.text = topic.title

		itemView.topicAuthor.text = topic.creation.user.login

		itemView.viewsCount.text = topic.stats.viewCount.toString()
		itemView.messagesCount.text = topic.stats.messageCount.toString()

		itemView.isClosed.isVisible = topic.isClosed ?: false
		itemView.isPinned.isVisible = topic.isPinned ?: false

		if (isExtendedList) {
			val lastMessage = topic.lastMessage.text
					.replace("(\r\n)+".toRegex(), "\n")
					.replace("\\[spoiler].*|\\[\\/spoiler]".toRegex(), "")
					.replace("\\[.*]".toRegex(), "")
					.replace(":\\w+:".toRegex(), "")
					.replace("\n", "")
			if (lastMessage.isNotEmpty()) itemView.lastMessage.text = lastMessage else itemView.lastMessage.visibility = View.GONE

			itemView.lastDate.text = topic.lastMessage.date.parseFullDate(true).getTimeAgo()
			itemView.lastUsername.text = topic.lastMessage.user.login

			itemView.divider.isVisible = true
			itemView.lastMessageBlock.isVisible = true
		} else {
			itemView.divider.isVisible = false
			itemView.lastMessageBlock.isVisible = false
		}
	}

	interface OnOpenContextMenu {
		fun onOpenContextMenu(topic: Forum.Topic)
	}

	companion object {
		private var listener: OnOpenContextMenu? = null
		private var isExtendedList: Boolean = false

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Forum.Topic, ForumTopicsViewHolder>,
				isExtendedList: Boolean
		): ForumTopicsViewHolder {
			this.isExtendedList = isExtendedList
			return ForumTopicsViewHolder(getView(viewGroup, R.layout.forum_topic_row_item), adapter)
		}

		fun setOnContextMenuListener(listener: OnOpenContextMenu) {
			this.listener = listener
		}
	}
}