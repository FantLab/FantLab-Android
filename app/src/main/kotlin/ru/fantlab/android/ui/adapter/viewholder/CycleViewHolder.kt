package ru.fantlab.android.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Cycle
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class CycleViewHolder : TreeViewBinder<CycleViewHolder.ViewHolder>() {

	override val layoutId: Int
		get() = R.layout.author_cycle_row_item

	override fun provideViewHolder(itemView: View): ViewHolder {
		return ViewHolder(itemView)
	}

	override fun bindView(
			holder: RecyclerView.ViewHolder, position: Int, node: TreeNode<*>
	) {
		(holder as CycleViewHolder.ViewHolder).ivArrow.rotation = 0f
		holder.ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp)
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
		val ivArrow: ImageView = rootView.findViewById<View>(R.id.iv_arrow) as ImageView
		var tvName: TextView = rootView.findViewById<View>(R.id.tv_name) as TextView

	}
}
