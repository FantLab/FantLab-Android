package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.work_analog_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.storage.WorkTypesProvider
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class AnalogsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<WorkAnalog, AnalogsViewHolder>)
	: BaseViewHolder<WorkAnalog>(itemView, adapter) {

	override fun bind(analog: WorkAnalog) {

		itemView.coverLayout.setUrl("https:${analog.imagePreview}", WorkTypesProvider.getCoverByTypeId(analog.nameTypeId))

		if (analog.creators.authors.isNotEmpty()) {
			itemView.authors.text = analog.creators.authors.joinToString(separator = ", "){ it.name }
			itemView.authors.visibility = View.VISIBLE
		} else {
			itemView.authors.visibility = View.GONE
		}

		itemView.title.text = if (analog.name.isNotEmpty())
			analog.name.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")
		else
			analog.nameOrig

		itemView.type.text = analog.nameType.capitalize()

		itemView.ratingBar.rating = analog.stat.rating.toFloatOrNull() ?: 0f
		itemView.rateMark.text = analog.stat.rating
		itemView.rateCount.text = "(${analog.stat.voters})"

		if (!InputHelper.isEmpty(analog.year)) {
			itemView.year.text = analog.year.toString() + " г."
		} else itemView.year.visibility = View.GONE
	}

	companion object {

		private val ANY_CHARACTERS_IN_BRACKETS_REGEX = "\\[.*?]".toRegex()

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<WorkAnalog, AnalogsViewHolder>
		): AnalogsViewHolder =
				AnalogsViewHolder(getView(viewGroup, R.layout.work_analog_row_item), adapter)
	}
}