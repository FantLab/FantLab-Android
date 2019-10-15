package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.work_classif_child_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ClassChild
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class ClassChildViewHolder : TreeViewBinder<ClassChildViewHolder.ViewHolder>() {

	override val layoutId: Int = R.layout.work_classif_child_row_item

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
		var tvName: TextView = rootView.tv_name
	}
}
