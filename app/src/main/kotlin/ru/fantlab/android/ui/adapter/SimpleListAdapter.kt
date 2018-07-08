package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.ui.adapter.viewholder.SimpleViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder


class SimpleListAdapter<T> constructor(data: ArrayList<T>)
	: BaseRecyclerAdapter<T, SimpleViewHolder<T>, BaseViewHolder.OnItemClickListener<T>>(data) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder<T>
			= SimpleViewHolder.newInstance(parent, this)

	override fun onBindView(holderEdition: SimpleViewHolder<T>, position: Int) {
		holderEdition.bind(getItem(position))
	}
}