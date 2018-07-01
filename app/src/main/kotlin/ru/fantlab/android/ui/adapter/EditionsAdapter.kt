package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.ui.adapter.viewholder.EditionsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class EditionsAdapter constructor(authors: ArrayList<EditionsBlocks.Edition>)
	: BaseRecyclerAdapter<EditionsBlocks.Edition, EditionsViewHolder, BaseViewHolder.OnItemClickListener<EditionsBlocks.Edition>>(authors) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): EditionsViewHolder
			= EditionsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: EditionsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}