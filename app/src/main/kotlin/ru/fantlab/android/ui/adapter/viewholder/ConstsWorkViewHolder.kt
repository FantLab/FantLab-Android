package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.consts_work_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Consts
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class ConstsWorkViewHolder : TreeViewBinder<ConstsWorkViewHolder.ViewHolder>() {

	override val layoutId: Int
		get() = R.layout.consts_work_row_item

	override fun provideViewHolder(itemView: View): ViewHolder {
		return ViewHolder(itemView)
	}

	override fun bindView(
			holder: RecyclerView.ViewHolder,
			position: Int,
			node: TreeNode<*>,
			onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		val nodeItem = node.content as Consts?
		(holder as ConstsWorkViewHolder.ViewHolder).title.text = nodeItem!!.title
		if (!nodeItem.description.isNullOrEmpty()) {
			holder.description.text = nodeItem.description
		} else holder.description.text = holder.itemView.context.getString(R.string.no_description)
	}

	inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		var title: TextView = rootView.title
		var description: TextView = rootView.description
	}
}
