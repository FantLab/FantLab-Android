package ru.fantlab.android.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.bookcase_header_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.BookcaseCategory
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class BookcaseHeaderViewHolder : TreeViewBinder<BookcaseHeaderViewHolder.ViewHolder>() {

    override val layoutId = R.layout.bookcase_header_item

    override fun provideViewHolder(itemView: View) = ViewHolder(itemView)

    override fun bindView(
            holder: RecyclerView.ViewHolder,
            position: Int,
            node: TreeNode<*>,
            onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
    ) {
        val nodeItem = node.content as BookcaseCategory?
        (holder as ViewHolder).title.text = nodeItem!!.name
        holder.expandButton.rotation = 0f
        holder.expandButton.setImageResource(R.drawable.ic_arrow_right)
        val rotateDegree = if (node.isExpand) 90f else 0f
        holder.expandButton.rotation = rotateDegree
        holder.expandButton.isVisible = !node.isLeaf
    }

    inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        val expandButton: ImageView = rootView.expandButton
        var title: TextView = rootView.category_name
    }
}
