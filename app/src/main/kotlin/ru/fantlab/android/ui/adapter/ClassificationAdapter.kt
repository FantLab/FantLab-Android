package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Classification
import ru.fantlab.android.ui.adapter.viewholder.ClassificationViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter

class ClassificationAdapter(authors: ArrayList<Classification>?)
	: BaseRecyclerAdapter<Classification, ClassificationViewHolder>(authors!!) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): ClassificationViewHolder = ClassificationViewHolder.newInstance(parent, this)

	override fun onBindView(holder: ClassificationViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}