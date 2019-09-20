package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.work_translation_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorkTranslation
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class WorkTranslationViewHolder : TreeViewBinder<WorkTranslationViewHolder.ViewHolder>() {

    override val layoutId = R.layout.work_translation_row_item

    override fun provideViewHolder(itemView: View) = ViewHolder(itemView)

    override fun bindView(
            holder: RecyclerView.ViewHolder,
            position: Int,
            node: TreeNode<*>,
            onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
    ) {
        val nodeItem = node.content as WorkTranslation?
        (holder as ViewHolder).translator.text = nodeItem!!.translation.translators.joinToString()
        holder.translation.text = nodeItem!!.translation.titles.joinToString()
    }

    inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        var translator: TextView = rootView.translator_name
        var translation: TextView = rootView.translation_details
    }
}
