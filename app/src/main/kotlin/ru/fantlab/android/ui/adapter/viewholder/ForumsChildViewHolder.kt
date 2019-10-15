package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.forums_child_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ForumsTreeChild
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.ui.modules.user.UserPagerActivity
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class ForumsChildViewHolder(private val isExtendedList: Boolean) : TreeViewBinder<ForumsChildViewHolder.ViewHolder>() {

	override val layoutId: Int = R.layout.forums_child_row_item

	override fun provideViewHolder(itemView: View): ViewHolder = ViewHolder(itemView)

	override fun bindView(
			holder: RecyclerView.ViewHolder, position: Int, node: TreeNode<*>, onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		(holder as ViewHolder)
		val parentNode = node.content as ForumsTreeChild?
		holder.title.text = parentNode!!.title
		holder.messagesCount.text = parentNode.messagesCount.toString()
		holder.topicsCount.text = parentNode.topicsCount.toString()

		if (isExtendedList) {
			holder.lastUsername.setOnClickListener { UserPagerActivity.startActivity(holder.itemView.context, parentNode.lastUsername, parentNode.lastUsernameId, 0) }
			holder.lastTopic.text = parentNode.lastTopic
			holder.lastDate.text = parentNode.lastDate.parseFullDate(true).getTimeAgo()
			holder.lastUsername.text = parentNode.lastUsername
			holder.lastMessageBlock.isVisible = true
			holder.divider.isVisible = true
		} else {
			holder.lastMessageBlock.isVisible = false
			holder.divider.isVisible = false
		}
	}

	class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		var title: TextView = rootView.forumTitle
		var lastTopic: TextView = rootView.lastTopic
		var lastDate: TextView = rootView.lastDate
		var lastUsername: TextView = rootView.lastUsername
		var messagesCount: TextView = rootView.messagesCount
		var topicsCount: TextView = rootView.topicsCount
		var lastMessageBlock: LinearLayout = rootView.lastMessageBlock
		var divider: View = rootView.divider
	}
}
