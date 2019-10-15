package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.edition_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.provider.storage.WorkTypesProvider
import ru.fantlab.android.ui.widgets.Dot
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class EditionsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<EditionsBlocks.Edition, EditionsViewHolder>)
	: BaseViewHolder<EditionsBlocks.Edition>(itemView, adapter) {

	override fun bind(edition: EditionsBlocks.Edition) {
		itemView.coverLayout.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("editions")
				.appendPath("big")
				.appendPath(edition.editionId.toString())
				.toString(), WorkTypesProvider.getCoverByTypeId(25))
		itemView.coverLayout.setDotColor(
				when {
					edition.planDate != null -> Dot.Color.GREY
					edition.correctLevel == 0f -> Dot.Color.RED
					edition.correctLevel == 0.5f -> Dot.Color.ORANGE
					edition.correctLevel == 1f -> Dot.Color.GREEN
					else -> throw IllegalStateException("Received invalid edition->correct_level from API")
				}
		)

		if (edition.authors.isNotEmpty()) {
			itemView.authors.text = edition.authors.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")
			itemView.authors.visibility = View.VISIBLE
		} else {
			itemView.authors.visibility = View.GONE
		}

		itemView.title.text = edition.name.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")

		if (edition.year.toString().isNotEmpty() && edition.year != 0) {
			itemView.year.text = edition.year.toString()
		} else {
			itemView.year.visibility = View.GONE
		}
	}

	companion object {

		private val ANY_CHARACTERS_IN_BRACKETS_REGEX = "\\[.*?]".toRegex()

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<EditionsBlocks.Edition, EditionsViewHolder>
		): EditionsViewHolder =
				EditionsViewHolder(getView(viewGroup, R.layout.edition_row_item), adapter)
	}
}