package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.profile_mark_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Mark
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.provider.storage.WorkTypesProvider
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class UserMarkViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Mark, UserMarkViewHolder>)
	: BaseViewHolder<Mark>(itemView, adapter) {

	override fun bind(mark: Mark) {
		itemView.coverLayout.setUrl("https:${mark.workImage}", WorkTypesProvider.getCoverByTypeId(mark.workTypeId))
		itemView.authors.text = mark.workAuthor
		itemView.title.text = if (mark.workName.isNotEmpty()) {
			if (mark.workNameOrig.isNotEmpty()) {
				String.format("%s / %s", mark.workName, mark.workNameOrig)
			} else {
				mark.workName
			}
		} else {
			mark.workNameOrig
		}
		itemView.type.text = mark.workType.capitalize()
		itemView.date.text = mark.dateIso.parseFullDate(true).getTimeAgo()
		if (mark.workYear != 0) {
			itemView.year.text = mark.workYear.toString()
			itemView.year.visibility = View.VISIBLE
		} else itemView.year.visibility = View.GONE
		itemView.mark.text = mark.mark.toString()
		itemView.classified.visibility = if (mark.userClassifiedWork == 1) View.VISIBLE else View.GONE
		itemView.response.visibility = if (mark.userResponseWork == 1) View.VISIBLE else View.GONE
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Mark, UserMarkViewHolder>
		): UserMarkViewHolder =
				UserMarkViewHolder(getView(viewGroup, R.layout.profile_mark_row_item), adapter)
	}
}