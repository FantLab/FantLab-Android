package ru.fantlab.android.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.bookcase_header_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.BookcaseCategory
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class BookcaseHeaderViewHolder : TreeViewBinder<BookcaseHeaderViewHolder.ViewHolder>() {

    override val layoutId: Int
        get() = R.layout.bookcase_header_item

    override fun provideViewHolder(itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun bindView(
            holder: RecyclerView.ViewHolder,
            position: Int,
            node: TreeNode<*>,
            onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
    ) {
        val nodeItem = node.content as BookcaseCategory?
        (holder as BookcaseViewHolder.ViewHolder).title.text = nodeItem!!.name
    }

    inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        var title: TextView = rootView.category_name
    }
}
