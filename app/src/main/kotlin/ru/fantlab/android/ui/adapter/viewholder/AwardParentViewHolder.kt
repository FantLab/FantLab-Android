package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.work_awards_parent_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorkAwardsParent
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class AwardParentViewHolder : TreeViewBinder<AwardParentViewHolder.ViewHolder>() {

	override val layoutId = R.layout.work_awards_parent_row_item

	override fun provideViewHolder(itemView: View) = ViewHolder(itemView)

	override fun bindView(
			holder: RecyclerView.ViewHolder, position: Int, node: TreeNode<*>, onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		(holder as ViewHolder).expandButton.rotation = 0f
		val rotateDegree = if (node.isExpand) 90f else 0f
		holder.expandButton.rotation = rotateDegree
		val parentNode = node.content as WorkAwardsParent
		holder.title.text = parentNode.title
		holder.expandButton.isVisible = !node.isLeaf
	}

	class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		val expandButton: ImageView = rootView.expandButton
		var title: FontTextView = rootView.awardsTitle
	}
}
