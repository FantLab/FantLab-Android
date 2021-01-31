package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cycle_content_child_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.CycleContentChild
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class CycleContentChildViewHolder : TreeViewBinder<CycleContentChildViewHolder.ViewHolder>() {

    override val layoutId: Int = R.layout.cycle_content_child_row_item

    override fun provideViewHolder(itemView: View) = ViewHolder(itemView)

    override fun bindView(
            holder: RecyclerView.ViewHolder,
            position: Int,
            node: TreeNode<*>,
            onTreeNodeListener:
            TreeViewAdapter.OnTreeNodeListener?
    ) {
        val childNode = node.content as CycleContentChild?
        (holder as CycleContentChildViewHolder.ViewHolder)
        childNode?.let {
            holder.apply {
                title.text = "▫ " + childNode.title
                if (childNode.rating != 0F) {
                    rating.text = childNode.rating.toString()
                    rating.isVisible = true
                } else rating.isVisible = false
                if (childNode.responses != 0) {
                    responses.text = childNode.responses.toString()
                    responses.isVisible = true
                } else responses.isVisible = false
            }
        }

    }

    inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        var title: FontTextView = rootView.title
        var rating: FontTextView = rootView.rating
        var responses: FontTextView = rootView.responses
    }
}
