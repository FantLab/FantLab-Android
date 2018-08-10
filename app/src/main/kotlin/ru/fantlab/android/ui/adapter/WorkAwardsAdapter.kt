package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.ui.adapter.viewholder.WorkAwardsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import kotlin.collections.ArrayList

class WorkAwardsAdapter (nom: ArrayList<Nomination>?)
	: BaseRecyclerAdapter<Nomination, WorkAwardsViewHolder, BaseViewHolder.OnItemClickListener<Nomination>>(nom!!) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): WorkAwardsViewHolder
			= WorkAwardsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: WorkAwardsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}