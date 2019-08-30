package ru.fantlab.android.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
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
        (holder as ViewHolder).title.text = nodeItem!!.bookcase.bookcaseName
        if (!nodeItem.bookcase.bookcaseComment.isNullOrEmpty()) {
            holder.description.text = nodeItem.bookcase.bookcaseComment
        } else holder.description.text = holder.itemView.context.getString(R.string.no_description)

        var url = "http://www.fantlab.ru/img/bc_mybooks.gif"
        if (nodeItem.bookcase.bookcaseType == "edition" && nodeItem.bookcase.bookcaseGroup == "sale") {
            url = "http://www.fantlab.ru/img/bc_sell.gif"
        } else if (nodeItem.bookcase.bookcaseType == "edition" && nodeItem.bookcase.bookcaseGroup == "buy") {
            url = "http://www.fantlab.ru/img/bc_buy.gif"
        } else if (nodeItem.bookcase.bookcaseGroup == "read") {
            url = "http://www.fantlab.ru/img/bc_toread.gif"
        } else if (nodeItem.bookcase.bookcaseType == "film") {
            url = "http://www.fantlab.ru/img/bc_kino.gif"
        }
        holder.coverLayout.setUrlGif(url)
        holder.count.text = nodeItem.bookcase.itemCount.toString()
    }

    inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        var title: TextView = rootView.bookcaseName
        var description: TextView = rootView.bookcaseDescription
        var coverLayout: CoverLayout = rootView.coverLayout
        var count: TextView = rootView.bookcaseCount
    }
}
