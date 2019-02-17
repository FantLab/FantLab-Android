package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.search_editions_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.SearchEdition
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class SearchEditionsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<SearchEdition, SearchEditionsViewHolder>)
	: BaseViewHolder<SearchEdition>(itemView, adapter) {

	override fun bind(edition: SearchEdition) {
		itemView.coverLayout.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("editions")
				.appendPath("big")
				.appendPath(edition.id.toString())
				.toString())

		if (edition.authors.isNotEmpty()) {
			itemView.authors.text = edition.authors.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")
			itemView.authors.visibility = View.VISIBLE
		} else {
			itemView.authors.visibility = View.GONE
		}

		itemView.title.text = edition.name.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")

		if (edition.year.isNotEmpty()) {
			itemView.year.text = edition.year
		} else {
			itemView.year.visibility = View.GONE
		}

	}

	companion object {

		private val ANY_CHARACTERS_IN_BRACKETS_REGEX = "\\[.*?]".toRegex()

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<SearchEdition, SearchEditionsViewHolder>
		): SearchEditionsViewHolder =
				SearchEditionsViewHolder(getView(viewGroup, R.layout.search_editions_row_item), adapter)
	}
}