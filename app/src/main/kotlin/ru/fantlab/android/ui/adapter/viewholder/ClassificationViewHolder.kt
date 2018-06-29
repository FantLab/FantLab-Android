package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ClassificationGenre
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class ClassificationViewHolder(itemView: View, adapter: BaseRecyclerAdapter<ClassificationGenre, ClassificationViewHolder, *>)
	: BaseViewHolder<ClassificationGenre>(itemView, adapter) {

	@BindView(R.id.label) lateinit var label: FontTextView

	override fun bind(classificatory: ClassificationGenre) {
        label.setLevel(classificatory.genreLevel)
        label.text = classificatory.label
        }

	companion object {

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<ClassificationGenre, ClassificationViewHolder, *>) : ClassificationViewHolder
				= ClassificationViewHolder(getView(viewGroup, R.layout.classification_row_item), adapter)
	}
}