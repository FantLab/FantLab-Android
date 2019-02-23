package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Publishers
import ru.fantlab.android.ui.adapter.viewholder.PublishersViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class PublishersAdapter constructor(publishers: ArrayList<Publishers.Publisher>)
	: BaseRecyclerAdapter<Publishers.Publisher, PublishersViewHolder>(publishers) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): PublishersViewHolder =
			PublishersViewHolder.newInstance(parent, this)

	override fun onBindView(holder: PublishersViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}