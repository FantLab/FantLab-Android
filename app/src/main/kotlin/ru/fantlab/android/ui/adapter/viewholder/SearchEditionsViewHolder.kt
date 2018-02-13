package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.SearchEditionModel
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.text.NumberFormat

class SearchEditionsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<SearchEditionModel, SearchEditionsViewHolder, *>)
	: BaseViewHolder<SearchEditionModel>(itemView, adapter) {

	@BindView(R.id.avatarLayout)
	lateinit var avatarLayout: AvatarLayout

	@BindView(R.id.authors)
	lateinit var authors: FontTextView

	@BindView(R.id.title)
	lateinit var title: FontTextView

	@BindView(R.id.year)
	lateinit var year: FontTextView

	private val numberFormat = NumberFormat.getNumberInstance()

	override fun bind(edition: SearchEditionModel) {
		avatarLayout.setUrl("https://data.fantlab.ru/images/editions/big/${edition.editionId}", "")
		if (edition.authors.isNotEmpty()) {
			authors.text = edition.authors.replace("\\[*]".toRegex(), "")
			authors.visibility = View.VISIBLE
		} else {
			authors.visibility = View.GONE
		}
		title.text = edition.name
		year.text = if (edition.year.isNotEmpty()) numberFormat.format(edition.year.toLong()) else "N/A"
	}

	companion object {

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<SearchEditionModel, SearchEditionsViewHolder, *>) : SearchEditionsViewHolder
				= SearchEditionsViewHolder(getView(viewGroup, R.layout.search_editions_row_item), adapter)
	}
}