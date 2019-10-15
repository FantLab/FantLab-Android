package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cycle_content_child_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.CycleContentChild
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class CycleContentChildViewHolder : TreeViewBinder<CycleContentChildViewHolder.ViewHolder>() {

	override val layoutId: Int = R.layout.cycle_content_child_row_item

	override fun provideViewHolder(itemView: View) = ViewHolder(itemView)

	override fun bindView(
			holder: RecyclerView.ViewHolder,
			position: Int,
			node: TreeNode<*>,
			onTreeNodeListener:
			TreeViewAdapter.OnTreeNodeListener?
	) {
		val childNode = node.content as CycleContentChild?
		(holder as CycleContentChildViewHolder.ViewHolder)
		holder.title.text = "⚬ " + childNode!!.title
	}

	inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		var title: FontTextView = rootView.title
	}
}
