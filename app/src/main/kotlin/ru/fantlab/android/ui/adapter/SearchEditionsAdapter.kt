package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.SearchEdition
import ru.fantlab.android.ui.adapter.viewholder.SearchEditionsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class SearchEditionsAdapter constructor(editions: ArrayList<SearchEdition>)
	: BaseRecyclerAdapter<SearchEdition, SearchEditionsViewHolder, BaseViewHolder.OnItemClickListener<SearchEdition>>(editions) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): SearchEditionsViewHolder
			= SearchEditionsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: SearchEditionsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}