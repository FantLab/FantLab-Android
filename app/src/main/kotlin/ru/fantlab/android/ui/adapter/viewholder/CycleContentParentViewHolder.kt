package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cycle_content_parent_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.CycleContentParent
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class CycleContentParentViewHolder : TreeViewBinder<CycleContentParentViewHolder.ViewHolder>() {

	override val layoutId: Int = R.layout.cycle_content_parent_row_item

	override fun provideViewHolder(itemView: View) = ViewHolder(itemView)

	override fun bindView(
			holder: RecyclerView.ViewHolder, position: Int, node: TreeNode<*>, onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		(holder as CycleContentParentViewHolder.ViewHolder)
		val parentNode = node.content as CycleContentParent?
		holder.title.text = "• " + parentNode!!.title
	}

	class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		var title: FontTextView = rootView.title
	}
}
