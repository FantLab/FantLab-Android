package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.AttachModel
import ru.fantlab.android.ui.adapter.viewholder.AttachesViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class AttachesAdapter constructor(responses: ArrayList<AttachModel>)
	: BaseRecyclerAdapter<AttachModel, AttachesViewHolder>(responses) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): AttachesViewHolder =
			AttachesViewHolder.newInstance(parent, this)

	override fun onBindView(holder: AttachesViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	fun setOnClickMenuListener(listener: AttachesViewHolder.OnClickMenu) {
		AttachesViewHolder.setOnClickMenuListener(listener)
	}
}