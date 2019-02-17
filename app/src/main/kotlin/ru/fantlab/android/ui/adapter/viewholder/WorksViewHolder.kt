package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.work_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class WorksViewHolder(itemView: View, adapter: BaseRecyclerAdapter<WorksBlocks.Work, WorksViewHolder>)
	: BaseViewHolder<WorksBlocks.Work>(itemView, adapter) {

	override fun bind(work: WorksBlocks.Work) {
		if (work.authors.isNotEmpty()) {
			itemView.authors.text = work.authors[0].name.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")
			itemView.authors.visibility = View.VISIBLE
		} else {
			itemView.authors.visibility = View.GONE
		}

		itemView.title.text = if (work.name.isNotEmpty()) {
			if (work.nameOrig.isNotEmpty()) {
				String.format("%s / %s", work.nameOrig, work.name)
			} else {
				work.name
			}
		} else {
			work.nameOrig
		}

		itemView.year.text = work.year?.toString() ?: "N/A"
	}

	companion object {

		private val ANY_CHARACTERS_IN_BRACKETS_REGEX = "\\[.*?]".toRegex()

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<WorksBlocks.Work, WorksViewHolder>
		): WorksViewHolder =
				WorksViewHolder(getView(viewGroup, R.layout.work_row_item), adapter)
	}
}