package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.work_analog_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class AnalogsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<WorkAnalog, AnalogsViewHolder>)
	: BaseViewHolder<WorkAnalog>(itemView, adapter) {

	override fun bind(analog: WorkAnalog) {
		itemView.coverLayout.setUrl("https:${analog.imagePreview}")

		itemView.title.text = if (analog.name.isNotEmpty())
			analog.name.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")
		else
			analog.nameOrig
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