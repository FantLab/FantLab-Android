package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.SearchWorkModel
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.text.NumberFormat

class SearchWorksViewHolder(itemView: View, adapter: BaseRecyclerAdapter<SearchWorkModel, SearchWorksViewHolder, *>)
	: BaseViewHolder<SearchWorkModel>(itemView, adapter) {

	@BindView(R.id.avatarLayout)
	lateinit var avatarLayout: AvatarLayout

	@BindView(R.id.authors)
	lateinit var authors: FontTextView

	@BindView(R.id.title)
	lateinit var title: FontTextView

	@BindView(R.id.year)
	lateinit var year: FontTextView

	@BindView(R.id.rating)
	lateinit var rating: FontTextView

	private val numberFormat = NumberFormat.getNumberInstance()

	override fun bind(work: SearchWorkModel) {
		val coverId = if (work.picEditionIdAuto.isNotEmpty()) {
			work.picEditionIdAuto.toInt()
		} else {
			0 // no image
		}
		avatarLayout.setUrl("https://data.fantlab.ru/images/editions/big/$coverId", "")
		authors.text = if (work.allAuthorRusName.isNotEmpty()) work.allAuthorRusName else work.allAuthorName
		title.text = StringBuilder()
				.append(if (work.rusName.isNotEmpty()) work.rusName else work.name)
				.append(if (work.name.isNotEmpty()) " / ${work.name}" else "")
		year.text = if (work.year != 0) work.year.toString() else "N/A"
		if (work.markCount != 0) {
			rating.text = String.format("%s / %s",
					numberFormat.format(work.midMark[0].toDouble()),
					numberFormat.format(work.markCount.toLong()))
			rating.visibility = View.VISIBLE
		} else {
			rating.visibility = View.GONE
		}
	}

	companion object {

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<SearchWorkModel, SearchWorksViewHolder, *>) : SearchWorksViewHolder
				= SearchWorksViewHolder(getView(viewGroup, R.layout.search_works_row_item), adapter)
	}
}