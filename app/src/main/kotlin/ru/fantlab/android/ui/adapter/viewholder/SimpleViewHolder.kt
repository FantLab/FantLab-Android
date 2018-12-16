package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.ui.adapter.SimpleListAdapter
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder


class SimpleViewHolder<T>(itemView: View, adapter: BaseRecyclerAdapter<T, SimpleViewHolder<T>>)
	: BaseViewHolder<T>(itemView, adapter) {

	@BindView(R.id.title) lateinit var title: FontTextView

	override fun bind(item: T) {
		title.text = item.toString()
	}

	companion object {
		fun <T> newInstance(
				viewGroup: ViewGroup,
				adapter: SimpleListAdapter<T>
		): SimpleViewHolder<T> =
				SimpleViewHolder(getView(viewGroup, R.layout.simple_row_item), adapter)
	}
}