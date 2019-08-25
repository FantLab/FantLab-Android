package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.classification_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Classification
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class ClassificationViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Classification, ClassificationViewHolder>)
	: BaseViewHolder<Classification>(itemView, adapter) {

	override fun bind(classification: Classification) {
		itemView.groupTitle.text = classification.title
		itemView.groupText.text = classification.groups.joinToString(separator = "\n"){ it }
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Classification, ClassificationViewHolder>
		): ClassificationViewHolder =
				ClassificationViewHolder(getView(viewGroup, R.layout.classification_row_item), adapter)
	}
}