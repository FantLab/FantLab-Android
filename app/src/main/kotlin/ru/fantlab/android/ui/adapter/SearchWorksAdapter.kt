package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.SearchWorkModel
import ru.fantlab.android.ui.adapter.viewholder.SearchWorksViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class SearchWorksAdapter constructor(works: ArrayList<SearchWorkModel>)
	: BaseRecyclerAdapter<SearchWorkModel, SearchWorksViewHolder, BaseViewHolder.OnItemClickListener<SearchWorkModel>>(works) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): SearchWorksViewHolder
			= SearchWorksViewHolder.newInstance(parent, this)

	override fun onBindView(holder: SearchWorksViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}