package ru.fantlab.android.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionContentParent
import ru.fantlab.android.provider.timeline.HtmlHelper
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class EditionContentParentViewHolder : TreeViewBinder<EditionContentParentViewHolder.ViewHolder>() {

	override val layoutId: Int
		get() = R.layout.edition_content_parent_row_item

	override fun provideViewHolder(itemView: View): ViewHolder {
		return ViewHolder(itemView)
	}

	override fun bindView(
			holder: RecyclerView.ViewHolder, position: Int, node: TreeNode<*>, onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		(holder as EditionContentParentViewHolder.ViewHolder).arrow.rotation = 0f
		holder.arrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp)
		val rotateDegree = if (node.isExpand) 90f else 0f
		holder.arrow.rotation = rotateDegree
		val parentNode = node.content as EditionContentParent?
		HtmlHelper.htmlIntoTextView(holder.title, parentNode!!.title, holder.title.width)
		if (node.isLeaf)
			holder.arrow.visibility = View.INVISIBLE
		else
			holder.arrow.visibility = View.VISIBLE
	}

	class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		val arrow: ImageView = rootView.findViewById<View>(R.id.arrow) as ImageView
		var title: TextView = rootView.findViewById<View>(R.id.title) as FontTextView
	}
}
