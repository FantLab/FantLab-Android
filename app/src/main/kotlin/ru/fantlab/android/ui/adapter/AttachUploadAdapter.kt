package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.AttachModel
import ru.fantlab.android.ui.adapter.viewholder.AttachUploadViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class AttachUploadAdapter constructor(responses: ArrayList<AttachModel>)
	: BaseRecyclerAdapter<AttachModel, AttachUploadViewHolder>(responses) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): AttachUploadViewHolder =
			AttachUploadViewHolder.newInstance(parent, this)

	override fun onBindView(holder: AttachUploadViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}