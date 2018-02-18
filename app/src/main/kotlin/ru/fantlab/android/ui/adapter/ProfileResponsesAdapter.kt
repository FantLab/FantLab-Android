package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.ui.adapter.viewholder.UserResponseViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class ProfileResponsesAdapter constructor(responses: ArrayList<Response>)
	: BaseRecyclerAdapter<Response, UserResponseViewHolder, BaseViewHolder.OnItemClickListener<Response>>(responses) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): UserResponseViewHolder =
			UserResponseViewHolder.newInstance(parent, this)

	override fun onBindView(holder: UserResponseViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}