package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Forum
import ru.fantlab.android.ui.adapter.viewholder.ForumTopicsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class ForumTopicsAdapter constructor(topics: ArrayList<Forum.Topic>, private val isExtendedList: Boolean)
	: BaseRecyclerAdapter<Forum.Topic, ForumTopicsViewHolder>(topics) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): ForumTopicsViewHolder =
			ForumTopicsViewHolder.newInstance(parent, this, isExtendedList)

	override fun onBindView(holder: ForumTopicsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	fun setOnContextMenuListener(listener: ForumTopicsViewHolder.OnOpenContextMenu) {
		ForumTopicsViewHolder.setOnContextMenuListener(listener)
	}
}