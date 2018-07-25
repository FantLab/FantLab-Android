package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.provider.timeline.HtmlHelper
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class EditionContentViewHolder(itemView: View, adapter: BaseRecyclerAdapter<String, EditionContentViewHolder, *>)
	: BaseViewHolder<String>(itemView, adapter) {

	@BindView(R.id.label) lateinit var label: FontTextView

	override fun bind(content: String) {
		HtmlHelper.htmlIntoTextView(label, content, label.width)
	}

	companion object {

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<String, EditionContentViewHolder, *>) : EditionContentViewHolder
				= EditionContentViewHolder(getView(viewGroup, R.layout.edition_content_row_item), adapter)
	}
}