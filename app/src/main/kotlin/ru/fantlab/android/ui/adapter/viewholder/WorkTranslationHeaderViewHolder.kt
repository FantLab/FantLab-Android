package ru.fantlab.android.ui.adapter.viewholder

import android.widget.TextView
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.work_translation_header_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorkTranslationLanguage
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder

class WorkTranslationHeaderViewHolder : TreeViewBinder<WorkTranslationHeaderViewHolder.ViewHolder>() {

    override val layoutId = R.layout.work_translation_header_item

    override fun provideViewHolder(itemView: View) = ViewHolder(itemView)

    override fun bindView(
            holder: RecyclerView.ViewHolder,
            position: Int,
            node: TreeNode<*>,
            onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
    ) {
        val nodeItem = node.content as WorkTranslationLanguage?
        (holder as ViewHolder).title.text = holder.itemView.context.getString(R.string.translation_language, nodeItem!!.translation.language)
    }

    inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        var title: FontTextView = rootView.translation_lang
    }
}
