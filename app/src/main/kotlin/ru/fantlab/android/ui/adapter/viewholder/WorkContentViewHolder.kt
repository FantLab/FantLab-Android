package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ChildWork
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class WorkContentViewHolder(itemView: View, adapter: BaseRecyclerAdapter<ChildWork, WorkContentViewHolder>)
	: BaseViewHolder<ChildWork>(itemView, adapter) {

	@BindView(R.id.workName) lateinit var label: FontTextView
	@BindView(R.id.workType) lateinit var type: FontTextView
	@BindView(R.id.rating) lateinit var rating: FontTextView
	@BindView(R.id.votes) lateinit var votes: FontTextView
	@BindView(R.id.year) lateinit var year: FontTextView

	override fun bind(content: ChildWork) {
		label.text = if (content.name.isNotEmpty()) {
			if (content.nameOrig.isNotEmpty()) {
				String.format("%s / %s", content.name, content.nameOrig)
			} else {
				content.name
			}
		} else {
			content.nameOrig
		}

		if (content.type != null) type.text = content.type.capitalize() else type.visibility = View.GONE

		if (InputHelper.isEmpty(content)) {
			year.text = content.year.toString()
		} else {
			year.visibility = View.GONE
		}

		if (!InputHelper.isEmpty(content.votersCount) && content.votersCount != 0) {
			rating.text = content.rating.toString()
			votes.text = content.votersCount.toString()
			rating.visibility = View.VISIBLE
			votes.visibility = View.VISIBLE
		} else {
			rating.visibility = View.GONE
			votes.visibility = View.GONE
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