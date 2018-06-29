package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.ClassificationGenre
import ru.fantlab.android.ui.adapter.viewholder.ClassificationViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class ClassificationAdapter constructor(classificatory: ArrayList<ClassificationGenre>)
	: BaseRecyclerAdapter<ClassificationGenre, ClassificationViewHolder, BaseViewHolder.OnItemClickListener<ClassificationGenre>>(classificatory) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): ClassificationViewHolder
			= ClassificationViewHolder.newInstance(parent, this)

	override fun onBindView(holder: ClassificationViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}