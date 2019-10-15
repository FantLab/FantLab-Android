package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.ui.adapter.viewholder.WorkEditionsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class WorkEditionsAdapter constructor(edition: ArrayList<EditionsBlocks.Edition>)
	: BaseRecyclerAdapter<EditionsBlocks.Edition, WorkEditionsViewHolder>(edition) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): WorkEditionsViewHolder = WorkEditionsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: WorkEditionsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}