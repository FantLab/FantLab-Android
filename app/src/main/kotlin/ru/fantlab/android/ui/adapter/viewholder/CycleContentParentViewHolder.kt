package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import androidx.core.view.isVisible
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
		parentNode?.let {
			val prefix = when (true) {
				it.deep == 1 && !node.isLeaf -> "☰"
				it.deep > 1 && !node.isLeaf -> "▪"
				it.deep == 1 && node.isLeaf -> "▬"
				else -> "▪"
			}
			holder.apply {
				title.text = prefix + " " + it.title
				if (it.rating != 0F) {
					rating.text = it.rating.toString()
					rating.isVisible = true
				} else rating.isVisible = false
				if (it.responses != 0) {
					responses.text = it.responses.toString()
					responses.isVisible = true
				} else responses.isVisible = false
			}
		}
	}

	class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		var title: FontTextView = rootView.title
		var rating: FontTextView = rootView.rating
		var responses: FontTextView = rootView.responses
	}
}
