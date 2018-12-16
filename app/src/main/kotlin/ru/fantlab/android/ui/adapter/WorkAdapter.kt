package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.ui.adapter.viewholder.WorksViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class WorkAdapter constructor(works: ArrayList<WorksBlocks.Work>)
	: BaseRecyclerAdapter<WorksBlocks.Work, WorksViewHolder>(works) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): WorksViewHolder = WorksViewHolder.newInstance(parent, this)

	override fun onBindView(holder: WorksViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}