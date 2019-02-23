package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Autplans
import ru.fantlab.android.ui.adapter.viewholder.AutplansViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class AutplansAdapter constructor(publishers: ArrayList<Autplans.Object>)
	: BaseRecyclerAdapter<Autplans.Object, AutplansViewHolder>(publishers) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): AutplansViewHolder =
			AutplansViewHolder.newInstance(parent, this)

	override fun onBindView(holder: AutplansViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}