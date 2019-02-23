package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Pubnews
import ru.fantlab.android.ui.adapter.viewholder.PubnewsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class PubnewsAdapter constructor(publishers: ArrayList<Pubnews.Object>)
	: BaseRecyclerAdapter<Pubnews.Object, PubnewsViewHolder>(publishers) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): PubnewsViewHolder =
			PubnewsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: PubnewsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}