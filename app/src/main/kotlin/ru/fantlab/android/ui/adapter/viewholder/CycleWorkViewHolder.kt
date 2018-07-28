package ru.fantlab.android.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.CycleWork
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class CycleWorkViewHolder : TreeViewBinder<CycleWorkViewHolder.ViewHolder>() {

	override val layoutId: Int
		get() = R.layout.author_cycle_work_row_item

	override fun provideViewHolder(itemView: View): ViewHolder {
		return ViewHolder(itemView)
	}

	override fun bindView(
			holder: RecyclerView.ViewHolder, position: Int, node: TreeNode<*>
	) {
		val fileNode = node.content as CycleWork?
		(holder as CycleWorkViewHolder.ViewHolder).tvName.text = fileNode!!.title
	}

	inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		var tvName: TextView = rootView.findViewById<View>(R.id.tv_name) as TextView

	}
}
