package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.ui.adapter.viewholder.AnalogsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class AnalogsAdapter constructor(analog: ArrayList<WorkAnalog>)
	: BaseRecyclerAdapter<WorkAnalog, AnalogsViewHolder, BaseViewHolder.OnItemClickListener<WorkAnalog>>(analog) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): AnalogsViewHolder
			= AnalogsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: AnalogsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}