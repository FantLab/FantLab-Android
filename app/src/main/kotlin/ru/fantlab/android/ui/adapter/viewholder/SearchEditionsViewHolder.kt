package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.newmodel.SearchEdition
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class SearchEditionsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<SearchEdition, SearchEditionsViewHolder, *>)
	: BaseViewHolder<SearchEdition>(itemView, adapter) {

	@BindView(R.id.coverLayout) lateinit var coverLayout: CoverLayout
	@BindView(R.id.authors) lateinit var authors: FontTextView
	@BindView(R.id.title) lateinit var title: FontTextView
	@BindView(R.id.year) lateinit var year: FontTextView

	override fun bind(edition: SearchEdition) {
		coverLayout.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("editions")
				.appendPath("big")
				.appendPath(edition.id.toString())
				.toString())

		if (edition.authors.isNotEmpty()) {
			authors.text = edition.authors.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")
			authors.visibility = View.VISIBLE
		} else {
			authors.visibility = View.GONE
		}

		title.text = edition.name.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")

		year.text = if (edition.year.isNotEmpty()) edition.year else "N/A"
	}

	companion object {

		private val ANY_CHARACTERS_IN_BRACKETS_REGEX = "\\[.*?]".toRegex()

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<SearchEdition, SearchEditionsViewHolder, *>) : SearchEditionsViewHolder
				= SearchEditionsViewHolder(getView(viewGroup, R.layout.search_editions_row_item), adapter)
	}
}