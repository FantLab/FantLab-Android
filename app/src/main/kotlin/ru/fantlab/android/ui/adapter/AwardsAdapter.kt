package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.AwardInList
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.ui.adapter.viewholder.AwardsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import kotlin.collections.ArrayList

class AwardsAdapter (nom: ArrayList<AwardInList>?)
	: BaseRecyclerAdapter<AwardInList, AwardsViewHolder, BaseViewHolder.OnItemClickListener<AwardInList>>(nom!!) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): AwardsViewHolder
			= AwardsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: AwardsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}