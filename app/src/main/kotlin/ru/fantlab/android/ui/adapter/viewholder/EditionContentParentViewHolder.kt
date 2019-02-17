package ru.fantlab.android.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.edition_content_parent_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionContentParent
import ru.fantlab.android.ui.widgets.htmlview.HTMLTextView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class EditionContentParentViewHolder : TreeViewBinder<EditionContentParentViewHolder.ViewHolder>() {

	override val layoutId: Int = R.layout.edition_content_parent_row_item

	override fun provideViewHolder(itemView: View): ViewHolder {
		return ViewHolder(itemView)
	}

	override fun bindView(
			holder: RecyclerView.ViewHolder, position: Int, node: TreeNode<*>, onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		(holder as EditionContentParentViewHolder.ViewHolder).arrow.rotation = 0f
		val parentNode = node.content as EditionContentParent?
		if (node.isLeaf){
			holder.arrow.visibility = View.INVISIBLE
			//if (node.isRoot && node.childList.isEmpty()) {}
		}
		else {
			holder.arrow.setImageResource(R.drawable.ic_arrow_right)
			val rotateDegree = if (node.isExpand) 90f else 0f
			holder.arrow.rotation = rotateDegree
			holder.arrow.visibility = View.VISIBLE
		}
		holder.title.text = parentNode!!.title
	}

	class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		val arrow: ImageView = rootView.arrow
		var title: HTMLTextView = rootView.title
	}
}
