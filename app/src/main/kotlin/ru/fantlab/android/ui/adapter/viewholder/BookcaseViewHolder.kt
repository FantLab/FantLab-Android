package ru.fantlab.android.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import ru.fantlab.android.R
import kotlinx.android.synthetic.main.bookcase_row_item.view.*
import ru.fantlab.android.data.dao.model.BookcaseChild
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class BookcaseViewHolder : TreeViewBinder<BookcaseViewHolder.ViewHolder>() {

    override val layoutId: Int
        get() = R.layout.bookcase_row_item

    override fun provideViewHolder(itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun bindView(
            holder: RecyclerView.ViewHolder,
            position: Int,
            node: TreeNode<*>,
            onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
    ) {
        val nodeItem = node.content as BookcaseChild?
        (holder as ViewHolder).title.text = nodeItem!!.name
        if (!nodeItem.description.isNullOrEmpty()) {
            holder.description.text = nodeItem.description
        } else holder.description.text = holder.itemView.context.getString(R.string.no_description)

        holder.coverLayout.setUrl("http://www.fantlab.ru/img/bc_mybooks.gif")
    }

    inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        var title: TextView = rootView.bookcaseName
        var description: TextView = rootView.bookcaseDescription
        var coverLayout: CoverLayout = rootView.coverLayout
    }
}
