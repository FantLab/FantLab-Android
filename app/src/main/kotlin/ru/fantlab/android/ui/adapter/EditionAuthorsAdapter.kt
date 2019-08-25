package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Edition
import ru.fantlab.android.ui.adapter.viewholder.EditionAuthorsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter

class EditionAuthorsAdapter(authors: ArrayList<Edition.Author>?)
	: BaseRecyclerAdapter<Edition.Author, EditionAuthorsViewHolder>(authors!!) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): EditionAuthorsViewHolder = EditionAuthorsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: EditionAuthorsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}