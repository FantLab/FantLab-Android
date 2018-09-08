package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.Dot
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class EditionsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<EditionsBlocks.Edition, EditionsViewHolder, *>)
	: BaseViewHolder<EditionsBlocks.Edition>(itemView, adapter) {

	@BindView(R.id.coverLayout) lateinit var coverLayout: CoverLayout
	@BindView(R.id.authors) lateinit var authors: FontTextView
	@BindView(R.id.title) lateinit var title: FontTextView
	@BindView(R.id.year) lateinit var year: FontTextView

	override fun bind(edition: EditionsBlocks.Edition) {
		coverLayout.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("editions")
				.appendPath("big")
				.appendPath(edition.editionId.toString())
				.toString())
		coverLayout.setDotColor(
				when {
					edition.planDate != null -> Dot.Color.GREY
					edition.correctLevel == 0f -> Dot.Color.RED
					edition.correctLevel == 0.5f -> Dot.Color.ORANGE
					edition.correctLevel == 1f -> Dot.Color.GREEN
					else -> throw IllegalStateException("Received invalid edition->correct_level from API")
				}
		)

		if (edition.authors.isNotEmpty()) {
			authors.text = edition.authors.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")
			authors.visibility = View.VISIBLE
		} else {
			authors.visibility = View.GONE
		}

		title.text = edition.name.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")

		year.text = if (edition.year.toString().isNotEmpty()) edition.year.toString() else "N/A"
	}

	companion object {

		private val ANY_CHARACTERS_IN_BRACKETS_REGEX = "\\[.*?]".toRegex()

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<EditionsBlocks.Edition, EditionsViewHolder, *>) : EditionsViewHolder
				= EditionsViewHolder(getView(viewGroup, R.layout.work_edition_row_item), adapter)
	}
}