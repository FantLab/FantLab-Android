package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Mark
import ru.fantlab.android.ui.adapter.viewholder.UserMarkViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class ProfileMarksAdapter constructor(marks: ArrayList<Mark>)
	: BaseRecyclerAdapter<Mark, UserMarkViewHolder, BaseViewHolder.OnItemClickListener<Mark>>(marks) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): UserMarkViewHolder {
		return UserMarkViewHolder(UserMarkViewHolder.getView(parent), this)
	}

	override fun onBindView(holder: UserMarkViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}