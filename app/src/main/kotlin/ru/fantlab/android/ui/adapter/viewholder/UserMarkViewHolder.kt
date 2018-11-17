package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Mark
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.ForegroundImageView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class UserMarkViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Mark, UserMarkViewHolder, *>)
	: BaseViewHolder<Mark>(itemView, adapter) {

	@BindView(R.id.coverLayout) lateinit var coverLayout: CoverLayout
	@BindView(R.id.authors) lateinit var author: FontTextView
	@BindView(R.id.title) lateinit var title: FontTextView
	@BindView(R.id.type) lateinit var type: FontTextView
	@BindView(R.id.year) lateinit var year: FontTextView
	@BindView(R.id.mark) lateinit var myMark: FontTextView
	@BindView(R.id.classified) lateinit var classified: ForegroundImageView
	@BindView(R.id.response) lateinit var response: ForegroundImageView

	override fun bind(mark: Mark) {
		coverLayout.setUrl(if (mark.workImage != null) "https:${mark.workImage}" else null)
		author.text = mark.workAuthor
		title.text = if (mark.workName.isNotEmpty()) {
			if (mark.workNameOrig.isNotEmpty()) {
				String.format("%s / %s", mark.workName, mark.workNameOrig)
			} else {
				mark.workName
			}
		} else {
			mark.workNameOrig
		}
		type.text = mark.workType.capitalize()
		if (mark.workYear != 0) {
			year.text = mark.workYear.toString()
		} else year.visibility = View.GONE
		myMark.text = mark.mark.toString()
		classified.visibility = if (mark.userClassifiedWork == 1) View.VISIBLE else View.GONE
		response.visibility = if (mark.userResponseWork == 1) View.VISIBLE else View.GONE
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Mark, UserMarkViewHolder, *>
		): UserMarkViewHolder =
				UserMarkViewHolder(getView(viewGroup, R.layout.profile_mark_row_item), adapter)
	}
}