package ru.fantlab.android.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ClassChild
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class ClassChildViewHolder : TreeViewBinder<ClassChildViewHolder.ViewHolder>() {

	override val layoutId: Int
		get() = R.layout.work_classif_child_row_item

	override fun provideViewHolder(itemView: View): ViewHolder {
		return ViewHolder(itemView)
	}

	override fun bindView(
			holder: RecyclerView.ViewHolder,
			position: Int,
			node: TreeNode<*>,
			onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		val childNode = node.content as ClassChild?
		(holder as ClassChildViewHolder.ViewHolder).tvName.text = childNode!!.title
	}

	inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		var tvName: TextView = rootView.findViewById<View>(R.id.tv_name) as TextView
	}
}
