package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class AnalogsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<WorkAnalog, AnalogsViewHolder, *>)
	: BaseViewHolder<WorkAnalog>(itemView, adapter) {

	@BindView(R.id.coverLayout) lateinit var coverLayout: CoverLayout
	@BindView(R.id.authors) lateinit var authors: FontTextView
	@BindView(R.id.title) lateinit var title: FontTextView
	@BindView(R.id.year) lateinit var year: FontTextView

	override fun bind(analog: WorkAnalog) {
		coverLayout.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("editions")
				.appendPath("big")
				.appendPath(analog.id.toString())
				.toString())

		if (analog.author1RusName.isNotEmpty()) {
			authors.text = analog.author1RusName.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")
			authors.visibility = View.VISIBLE
		} else {
			authors.visibility = View.GONE
		}

		title.text = analog.rusName.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")

		year.text = if (analog.year.toString().isNotEmpty()) analog.year.toString() else "N/A"
	}

	companion object {

		private val ANY_CHARACTERS_IN_BRACKETS_REGEX = "\\[.*?]".toRegex()

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<WorkAnalog, AnalogsViewHolder, *>) : AnalogsViewHolder
				= AnalogsViewHolder(getView(viewGroup, R.layout.work_analog_row_item), adapter)
	}
}