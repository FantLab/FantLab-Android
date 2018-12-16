package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.ChildWork
import ru.fantlab.android.ui.adapter.viewholder.WorkContentViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class WorkContentAdapter constructor(content: ArrayList<ChildWork>)
	: BaseRecyclerAdapter<ChildWork, WorkContentViewHolder>(content) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): WorkContentViewHolder = WorkContentViewHolder.newInstance(parent, this)

	override fun onBindView(holderWork: WorkContentViewHolder, position: Int) {
		holderWork.bind(getItem(position))
	}
}