package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Work
import ru.fantlab.android.ui.adapter.viewholder.WorkAuthorsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter

class WorkAuthorsAdapter(authors: ArrayList<Work.Author>?)
	: BaseRecyclerAdapter<Work.Author, WorkAuthorsViewHolder>(authors!!) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): WorkAuthorsViewHolder = WorkAuthorsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: WorkAuthorsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}