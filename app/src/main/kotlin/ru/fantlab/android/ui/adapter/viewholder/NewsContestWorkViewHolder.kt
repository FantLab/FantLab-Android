package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contest_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.NewsContest
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class NewsContestWorkViewHolder : TreeViewBinder<NewsContestWorkViewHolder.ViewHolder>() {

	override val layoutId = R.layout.contest_row_item

	override fun provideViewHolder(itemView: View) = ViewHolder(itemView)

	override fun bindView(
			holder: RecyclerView.ViewHolder,
			position: Int,
			node: TreeNode<*>,
			onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		val nodeItem = node.content as NewsContest
		(holder as NewsContestWorkViewHolder.ViewHolder).title.text = nodeItem.title
		/*if (!nodeItem.description.isNullOrEmpty()) {
			holder.description.text = nodeItem.description
		}*/
		holder.place.text = "${nodeItem.place} место"
	}

	inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		var title: TextView = rootView.title
		//var description: TextView = rootView.description
		var place: TextView = rootView.place
	}
}
