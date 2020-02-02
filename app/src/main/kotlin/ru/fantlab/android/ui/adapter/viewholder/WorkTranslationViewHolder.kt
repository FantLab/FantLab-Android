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
import kotlin.math.min

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
        // Assuming, that 5 translators is maximum
        val translatorsLen = min(nodeItem!!.translation.translators.size, 5)
        for (translatorIndex in 0 until translatorsLen) {
            (holder as ViewHolder).translatorWidgets[translatorIndex].visibility = View.VISIBLE
            var translatorName = nodeItem.translation.translators[translatorIndex].shortName
            if (translatorIndex < translatorsLen -1) {
                translatorName += ","
            }
            holder.translatorWidgets[translatorIndex].text = translatorName
        }
        (holder as ViewHolder).translation.text = holder.itemView.context.getString(R.string.translation_details,
                nodeItem.translation.titles[0].title,
                nodeItem.translation.year,
                nodeItem.translation.titles[0].editionCount)
    }

    inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        var translatorWidgets = arrayListOf(rootView.translator1, rootView.translator2,
                rootView.translator3, rootView.translator4, rootView.translator5)
        var translation: TextView = rootView.translation_details
    }
}
