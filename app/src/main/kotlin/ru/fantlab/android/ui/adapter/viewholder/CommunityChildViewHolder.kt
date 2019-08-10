package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.community_child_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.CommunityTreeChild
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.ui.widgets.ForegroundImageView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class CommunityChildViewHolder : TreeViewBinder<CommunityChildViewHolder.ViewHolder>() {

	override val layoutId: Int = R.layout.community_child_row_item

	override fun provideViewHolder(itemView: View): ViewHolder = ViewHolder(itemView)

	override fun bindView(
			holder: RecyclerView.ViewHolder , position: Int, node: TreeNode<*>, onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		(holder as ViewHolder)
		val parentNode = node.content as CommunityTreeChild

		Glide.with(holder.itemView.context).load(parentNode.avatar).into(holder.communityAvatar)
		holder.communityTitle.text = parentNode.title
		holder.articleCount.text = parentNode.articleCount.toString()
		holder.subscriberCount.text = parentNode.subscriberCount.toString()
		holder.lastDate.text = parentNode.lastArticleDate.parseFullDate(true).getTimeAgo()
		holder.lastUsername.text = parentNode.lastUsernameLogin
		holder.lastArticleTitle.text = parentNode.lastArticleTitle


	}

	class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		var communityAvatar: ForegroundImageView = rootView.communityAvatar
		var communityTitle: TextView = rootView.communityTitle
		var articleCount: TextView = rootView.articleCount
		var subscriberCount: TextView = rootView.subscriberCount
		var lastDate: TextView = rootView.lastDate
		var lastArticleTitle: TextView = rootView.lastArticleTitle
		var lastUsername: TextView = rootView.lastUsername
	}
}
