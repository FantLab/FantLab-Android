package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.ForegroundImageView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder


class ContextMenuViewHolder(itemView: View, adapter: BaseRecyclerAdapter<ContextMenus.MenuItem, ContextMenuViewHolder>)
	: BaseViewHolder<ContextMenus.MenuItem>(itemView, adapter) {

	@BindView(R.id.icon) lateinit var icon: ForegroundImageView
	@BindView(R.id.title) lateinit var title: FontTextView

	override fun bind(item: ContextMenus.MenuItem) {
		icon.setImageResource(item.icon)
		title.text = item.title
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<ContextMenus.MenuItem, ContextMenuViewHolder>
		): ContextMenuViewHolder =
				ContextMenuViewHolder(getView(viewGroup, R.layout.context_row_item), adapter)
	}
}