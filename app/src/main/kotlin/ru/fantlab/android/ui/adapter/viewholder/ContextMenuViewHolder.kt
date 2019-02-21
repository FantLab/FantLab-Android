package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.context_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder


class ContextMenuViewHolder(itemView: View, adapter: BaseRecyclerAdapter<ContextMenus.MenuItem, ContextMenuViewHolder>)
	: BaseViewHolder<ContextMenus.MenuItem>(itemView, adapter) {

	override fun bind(item: ContextMenus.MenuItem) {
		if (item.icon != null) {
			itemView.icon.setImageResource(item.icon)
			itemView.icon.visibility = View.VISIBLE
		} else itemView.icon.visibility = View.GONE

		itemView.title.text = item.title
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<ContextMenus.MenuItem, ContextMenuViewHolder>
		): ContextMenuViewHolder =
				ContextMenuViewHolder(getView(viewGroup, R.layout.context_row_item), adapter)
	}
}