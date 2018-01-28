package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.ForumMessage
import ru.fantlab.android.ui.adapter.viewholder.ForumMessageViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class ForumFeedAdapter @JvmOverloads constructor(messages: ArrayList<ForumMessage>, private val noImage: Boolean = false)
	: BaseRecyclerAdapter<ForumMessage, ForumMessageViewHolder, BaseViewHolder.OnItemClickListener<ForumMessage>>(messages) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): ForumMessageViewHolder {
		return ForumMessageViewHolder(ForumMessageViewHolder.getView(parent, noImage), this)
	}

	override fun onBindView(holder: ForumMessageViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}
