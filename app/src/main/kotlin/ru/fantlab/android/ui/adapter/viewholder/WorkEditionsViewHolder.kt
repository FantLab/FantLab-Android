package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.work_edition_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.provider.storage.WorkTypesProvider
import ru.fantlab.android.ui.modules.edition.EditionActivity
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class WorkEditionsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<EditionsBlocks.Edition, WorkEditionsViewHolder>)
	: BaseViewHolder<EditionsBlocks.Edition>(itemView, adapter) {

	override fun bind(edition: EditionsBlocks.Edition) {
		itemView.editionCover.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("editions")
				.appendPath("small")
				.appendPath(edition.editionId.toString())
				.toString(), WorkTypesProvider.getCoverByTypeId(25))

		if (edition.year.toString().isNotEmpty() && edition.year != 0) {
			itemView.editionYear.text = edition.year.toString()
			itemView.editionYear.visibility = View.VISIBLE
		} else {
			itemView.editionYear.visibility = View.GONE
		}

		itemView.setOnClickListener { EditionActivity.startActivity(itemView.context, edition.editionId, edition.name) }
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<EditionsBlocks.Edition, WorkEditionsViewHolder>
		): WorkEditionsViewHolder =
				WorkEditionsViewHolder(getView(viewGroup, R.layout.work_edition_row_item), adapter)
	}
}