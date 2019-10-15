package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.author_cycle_row_item.view.*

import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Cycle
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class CycleViewHolder : TreeViewBinder<CycleViewHolder.ViewHolder>() {

	override val layoutId: Int
		get() = R.layout.author_cycle_row_item

	override fun provideViewHolder(itemView: View): ViewHolder {
		return ViewHolder(itemView)
	}

	override fun bindView(
			holder: RecyclerView.ViewHolder,
			position: Int,
			node: TreeNode<*>,
			onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		(holder as CycleViewHolder.ViewHolder).ivArrow.rotation = 0f
		holder.ivArrow.setImageResource(R.drawable.ic_arrow_right)
		val rotateDegree = if (node.isExpand) 90f else 0f
		holder.ivArrow.rotation = rotateDegree
		val dirNode = node.content as Cycle?
		holder.tvName.text = dirNode!!.cycleName
		if (node.isLeaf)
			holder.ivArrow.visibility = View.INVISIBLE
		else
			holder.ivArrow.visibility = View.VISIBLE
	}

	class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		val ivArrow: ImageView = rootView.iv_arrow
		var tvName: TextView = rootView.tv_name
	}
}
