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
		itemView.workName.text = if (content.name.isNotEmpty()) {
			if (content.nameOrig.isNotEmpty()) {
				String.format("%s / %s", content.name, content.nameOrig)
			} else {
				content.name
			}
		} else {
			content.nameOrig
		}

		if (content.type != null) itemView.workType.text = content.type.capitalize() else itemView.workType.visibility = View.GONE

		if (InputHelper.isEmpty(content)) {
			itemView.year.text = content.year.toString()
		} else {
			itemView.year.visibility = View.GONE
		}

		if (!InputHelper.isEmpty(content.votersCount) && content.votersCount != 0) {
			itemView.rating.text = content.rating.toString()
			itemView.votes.text = content.votersCount.toString()
			itemView.rating.visibility = View.VISIBLE
			itemView.votes.visibility = View.VISIBLE
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