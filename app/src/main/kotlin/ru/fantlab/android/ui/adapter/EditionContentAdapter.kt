package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.ui.adapter.viewholder.EditionContentViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class EditionContentAdapter constructor(content: ArrayList<String>)
	: BaseRecyclerAdapter<String, EditionContentViewHolder, BaseViewHolder.OnItemClickListener<String>>(content) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): EditionContentViewHolder
			= EditionContentViewHolder.newInstance(parent, this)

	override fun onBindView(holderEdition: EditionContentViewHolder, position: Int) {
		holderEdition.bind(getItem(position))
	}
}