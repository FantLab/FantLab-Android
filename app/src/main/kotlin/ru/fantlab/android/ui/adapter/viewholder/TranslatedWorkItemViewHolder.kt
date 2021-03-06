package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.translated_work_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionCard
import ru.fantlab.android.data.dao.model.TranslatedWorkItem
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class TranslatedWorkItemViewHolder : TreeViewBinder<TranslatedWorkItemViewHolder.ViewHolder>() {

    override val layoutId = R.layout.translated_work_row_item

    override fun provideViewHolder(itemView: View) = ViewHolder(itemView)

    override fun bindView(
            holder: RecyclerView.ViewHolder,
            position: Int,
            node: TreeNode<*>,
            onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
    ) {
        val nodeItem = node.content as TranslatedWorkItem?
        (holder as ViewHolder).author.text = nodeItem!!.translatedWork.author.name
        holder.work.text = nodeItem.translatedWork.work.name
        val year = getEditionYear(nodeItem.translatedWork.editions)
        if (year == 0) {
            holder.details.text = "(" + nodeItem.translatedWork.work.nameType + ")"
        } else {
            holder.details.text = "(" + year + ", " + nodeItem.translatedWork.work.nameType + ")"
        }
    }

    private fun getEditionYear(editions: ArrayList<EditionCard>): Int {
        var res = 0
        if (editions.isNotEmpty()) {
            for (edition in editions) {
                if (edition != null) {
                    res = edition.year
                    break
                }
            }
        }

        return res
    }

    inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        var author: TextView = rootView.author
        var work: TextView = rootView.work_rus
        var details: TextView = rootView.work_details
    }
}
