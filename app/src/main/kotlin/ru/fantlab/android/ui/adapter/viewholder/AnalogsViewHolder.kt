package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.helper.InputHelper
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
		coverLayout.setUrl("https:${analog.imagePreview}")

		if (analog.creators.authors.isNotEmpty()) {
			authors.text = analog.creators.authors.joinToString(separator = ", "){ it.name }
			authors.visibility = View.VISIBLE
		} else {
			authors.visibility = View.GONE
		}

		title.text = if (analog.name.isNotEmpty())
			analog.name.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")
		else
			analog.nameOrig

		if (!InputHelper.isEmpty(analog.year)) {
			year.text = analog.year.toString()
		} else year.visibility = View.GONE
	}

	companion object {

		private val ANY_CHARACTERS_IN_BRACKETS_REGEX = "\\[.*?]".toRegex()

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<WorkAnalog, AnalogsViewHolder, *>
		): AnalogsViewHolder =
				AnalogsViewHolder(getView(viewGroup, R.layout.work_analog_row_item), adapter)
	}
}