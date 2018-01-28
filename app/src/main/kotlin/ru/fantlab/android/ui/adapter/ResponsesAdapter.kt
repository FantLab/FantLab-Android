package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.ui.adapter.viewholder.ResponseViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class ResponsesAdapter @JvmOverloads constructor(events: ArrayList<Response>, private val noImage: Boolean = false)
	: BaseRecyclerAdapter<Response, ResponseViewHolder, BaseViewHolder.OnItemClickListener<Response>>(events) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): ResponseViewHolder {
		return ResponseViewHolder(ResponseViewHolder.getView(parent, noImage), this)
	}

	override fun onBindView(holder: ResponseViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}
