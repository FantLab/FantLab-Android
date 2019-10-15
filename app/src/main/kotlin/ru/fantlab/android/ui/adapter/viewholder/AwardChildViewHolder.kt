package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.work_awards_child_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorkAwardsChild
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.ForegroundImageView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class AwardChildViewHolder : TreeViewBinder<AwardChildViewHolder.ViewHolder>() {

	override val layoutId: Int = R.layout.work_awards_child_row_item

	override fun provideViewHolder(itemView: View) = ViewHolder(itemView)

	override fun bindView(
			holder: RecyclerView.ViewHolder,
			position: Int,
			node: TreeNode<*>,
			onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		val childNode = node.content as WorkAwardsChild?
		holder as AwardChildViewHolder.ViewHolder
		if (childNode != null) {
			Glide.with(holder.itemView.context).load("https://${LinkParserHelper.HOST_DATA}/images/awards/${childNode.awardId}")
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(holder.awardIcon)

			holder.nameRus.text = childNode.nameRus

			if (childNode.nameOrig.isNotEmpty()) {
				holder.name.text = childNode.nameOrig
				holder.name.visibility = View.VISIBLE
			} else holder.name.visibility = View.GONE

			if (!childNode.type.isNullOrEmpty()) {
				holder.type.text = childNode.type
				holder.type.visibility = View.VISIBLE
			} else holder.type.visibility = View.GONE
			holder.date.text = childNode.date.toString()
		}
	}

	inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
		var awardIcon: ForegroundImageView = rootView.awardIcon
		var name: FontTextView = rootView.name
		var nameRus: FontTextView = rootView.nameRus
		var country: FontTextView = rootView.country
		var type: FontTextView = rootView.type
		var date: FontTextView = rootView.date
	}
}
