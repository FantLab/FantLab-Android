package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.AuthorInList
import ru.fantlab.android.ui.adapter.viewholder.AuthorsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class AuthorsAdapter constructor(authors: ArrayList<AuthorInList>)
	: BaseRecyclerAdapter<AuthorInList, AuthorsViewHolder, BaseViewHolder.OnItemClickListener<AuthorInList>>(authors) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): AuthorsViewHolder = AuthorsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: AuthorsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}