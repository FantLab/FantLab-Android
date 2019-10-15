package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.edition_content_parent_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionContentParent
import ru.fantlab.android.ui.widgets.htmlview.HTMLTextView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class EditionContentParentViewHolder : TreeViewBinder<EditionContentParentViewHolder.ViewHolder>() {

	override val layoutId: Int = R.layout.edition_content_parent_row_item

	override fun provideViewHolder(itemView: View) = ViewHolder(itemView)

	override fun bindView(
			holder: RecyclerView.ViewHolder, position: Int, node: TreeNode<*>, onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		(holder as EditionContentParentViewHolder.ViewHolder)
		val parentNode = node.content as EditionContentParent?
		holder.title.html = "• " + parentNode!!.title
	}

	class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		var title: HTMLTextView = rootView.title
	}
}
