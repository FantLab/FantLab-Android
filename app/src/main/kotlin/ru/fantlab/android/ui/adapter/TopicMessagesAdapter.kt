package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import kotlinx.android.synthetic.main.topic_message_row_item.view.*
import ru.fantlab.android.data.dao.model.ForumTopic
import ru.fantlab.android.ui.adapter.viewholder.TopicMessagesViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class TopicMessagesAdapter constructor(topics: ArrayList<ForumTopic.Message>)
	: BaseRecyclerAdapter<ForumTopic.Message, TopicMessagesViewHolder>(topics) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): TopicMessagesViewHolder =
			TopicMessagesViewHolder.newInstance(parent, this)

	override fun onBindView(holder: TopicMessagesViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	fun setOnContextMenuListener(listener: TopicMessagesViewHolder.MessageMenu) {
		TopicMessagesViewHolder.setOnMessageMenuListener(listener)
	}

	// https://code.google.com/p/android/issues/detail?id=208169
	override fun onViewAttachedToWindow(holder: TopicMessagesViewHolder) {
		holder.itemView.messageText.isEnabled  = false
		holder.itemView.messageText.isEnabled  = true
	}
}