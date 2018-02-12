package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.SearchAuthorsModel
import ru.fantlab.android.ui.adapter.viewholder.SearchAuthorsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class SearchAuthorsAdapter constructor(authors: ArrayList<SearchAuthorsModel>)
	: BaseRecyclerAdapter<SearchAuthorsModel, SearchAuthorsViewHolder, BaseViewHolder.OnItemClickListener<SearchAuthorsModel>>(authors) {
	
	override fun viewHolder(parent: ViewGroup, viewType: Int): SearchAuthorsViewHolder
			= SearchAuthorsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: SearchAuthorsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}