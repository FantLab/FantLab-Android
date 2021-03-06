package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.SearchAward
import ru.fantlab.android.ui.adapter.viewholder.SearchAwardsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class SearchAwardsAdapter constructor(awards: ArrayList<SearchAward>)
	: BaseRecyclerAdapter<SearchAward, SearchAwardsViewHolder>(awards) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): SearchAwardsViewHolder = SearchAwardsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: SearchAwardsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}