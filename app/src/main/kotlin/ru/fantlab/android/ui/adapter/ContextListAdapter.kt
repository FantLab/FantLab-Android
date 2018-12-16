package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.ui.adapter.viewholder.ContextMenuViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter


class ContextListAdapter constructor(menu: ArrayList<ContextMenus.MenuItem>)
	: BaseRecyclerAdapter<ContextMenus.MenuItem, ContextMenuViewHolder>(menu) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): ContextMenuViewHolder = ContextMenuViewHolder.newInstance(parent, this)

	override fun onBindView(holderEdition: ContextMenuViewHolder, position: Int) {
		holderEdition.bind(getItem(position))
	}
}