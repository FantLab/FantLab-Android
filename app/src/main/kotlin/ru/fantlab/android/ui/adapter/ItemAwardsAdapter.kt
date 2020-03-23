package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.ui.adapter.viewholder.ItemAwardsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter

class ItemAwardsAdapter(nom: ArrayList<Nomination>)
	: BaseRecyclerAdapter<Nomination, ItemAwardsViewHolder>(nom) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): ItemAwardsViewHolder = ItemAwardsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: ItemAwardsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}