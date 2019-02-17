package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.ui.adapter.viewholder.WorkResponseViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class WorkResponsesAdapter constructor(responses: ArrayList<Response>)
	: BaseRecyclerAdapter<Response, WorkResponseViewHolder>(responses) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): WorkResponseViewHolder =
			WorkResponseViewHolder.newInstance(parent, this)

	override fun onBindView(holder: WorkResponseViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	fun setOnContextMenuListener(listener: WorkResponseViewHolder.OnOpenContextMenu) {
		WorkResponseViewHolder.setOnContextMenuListener(listener)
	}
}