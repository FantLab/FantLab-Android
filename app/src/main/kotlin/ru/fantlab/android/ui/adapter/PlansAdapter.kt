package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Pubplans
import ru.fantlab.android.ui.adapter.viewholder.PubplansViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class PlansAdapter constructor(publishers: ArrayList<Pubplans.Object>)
	: BaseRecyclerAdapter<Pubplans.Object, PubplansViewHolder>(publishers) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): PubplansViewHolder =
			PubplansViewHolder.newInstance(parent, this)

	override fun onBindView(holder: PubplansViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}