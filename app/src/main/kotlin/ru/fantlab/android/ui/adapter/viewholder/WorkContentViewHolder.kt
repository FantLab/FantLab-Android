package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.work_content_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ChildWork
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class WorkContentViewHolder(itemView: View, adapter: BaseRecyclerAdapter<ChildWork, WorkContentViewHolder>)
	: BaseViewHolder<ChildWork>(itemView, adapter) {

	override fun bind(content: ChildWork) {
		itemView.workName.text = if (content.name.isNotEmpty()) content.name else content.nameOrig

		if (content.type != null) {
			itemView.workType.text = content.type.capitalize()
			itemView.workType.visibility = View.VISIBLE
		} else itemView.workType.visibility = View.GONE

		if (!InputHelper.isEmpty(content.year)) {
			itemView.year.text = content.year.toString()
			itemView.year.visibility = View.VISIBLE
		} else {
			if (!InputHelper.isEmpty(content.publishStatus)) {
				itemView.year.text = content.publishStatus.capitalize()
				itemView.year.visibility = View.VISIBLE
			} else itemView.year.visibility = View.GONE
		}

		if (!InputHelper.isEmpty(content.description)) {
			itemView.description.text = content.description
			itemView.description.visibility = View.VISIBLE
		} else {
			itemView.description.visibility = View.GONE
		}

		if (!InputHelper.isEmpty(content.votersCount) && !InputHelper.isEmpty(content.rating)) {
			itemView.rating.text = content.rating.toString()
			itemView.votes.text = content.votersCount.toString()
			itemView.votes.visibility = View.VISIBLE
			itemView.rating.visibility = View.VISIBLE
		} else {
			itemView.rating.visibility = View.GONE
			itemView.votes.visibility = View.GONE
		}
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<ChildWork, WorkContentViewHolder>
		): WorkContentViewHolder =
				WorkContentViewHolder(getView(viewGroup, R.layout.work_content_row_item), adapter)
	}
}