package ru.fantlab.android.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionContentChild
import ru.fantlab.android.ui.widgets.htmlview.HTMLTextView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class EditionContentChildViewHolder : TreeViewBinder<EditionContentChildViewHolder.ViewHolder>() {

	override val layoutId: Int
		get() = R.layout.edition_content_child_row_item

	override fun provideViewHolder(itemView: View) = ViewHolder(itemView)

	override fun bindView(
			holder: RecyclerView.ViewHolder,
			position: Int,
			node: TreeNode<*>,
			onTreeNodeListener:
			TreeViewAdapter.OnTreeNodeListener?
	) {
		val childNode = node.content as EditionContentChild?
		(holder as EditionContentChildViewHolder.ViewHolder)
		holder.title.html = childNode!!.title
	}

	inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		var title: HTMLTextView = rootView.findViewById<View>(R.id.title) as HTMLTextView
	}
}
