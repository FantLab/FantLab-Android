package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.ui.adapter.viewholder.ResponseViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class ResponsesAdapter constructor(responses: ArrayList<Response>)
	: BaseRecyclerAdapter<Response, ResponseViewHolder>(responses) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): ResponseViewHolder =
			ResponseViewHolder.newInstance(parent, this)

	override fun onBindView(holder: ResponseViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	fun setOnContextMenuListener(listener: ResponseViewHolder.OnOpenContextMenu) {
		ResponseViewHolder.setOnContextMenuListener(listener)
	}
}