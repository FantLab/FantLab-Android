package ru.fantlab.android.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.CycleWork
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder
import java.text.NumberFormat

class CycleWorkViewHolder : TreeViewBinder<CycleWorkViewHolder.ViewHolder>() {
	private val numberFormat = NumberFormat.getNumberInstance()

	override val layoutId = R.layout.author_cycle_work_row_item

	override fun provideViewHolder(itemView: View): ViewHolder {
		return ViewHolder(itemView)
	}

	override fun bindView(
			holder: RecyclerView.ViewHolder, position: Int, node: TreeNode<*>, onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		val work = (node.content as CycleWork)
		(holder as CycleWorkViewHolder.ViewHolder)
			if (work.authors.size > 1) {
				holder.authors.text = work.authors.joinToString { it.name }
				holder.authors.visibility = View.VISIBLE
			} else {
				holder.authors.visibility = View.GONE
			}

		holder.title.text = if (work.name.isNotEmpty()) {
				if (work.nameOrig.isNotEmpty()) {
					String.format("%s / %s", work.name, work.nameOrig)
				} else {
					work.name
				}
			} else {
				work.nameOrig
			}

			if (!InputHelper.isEmpty(work.description)) {
				holder.description.text = work.description?.let { work.description.replace("\\[(.*?)]".toRegex(), "") }
			} else holder.description.visibility = View.GONE

			if (!InputHelper.isEmpty(work.year)) {
				holder.year.text = work.year.toString()
			} else holder.year.visibility = View.GONE

			if (work.responseCount != null && work.responseCount != 0) {
				holder.responses.text = work.responseCount.toString()
				holder.responses.visibility = View.VISIBLE
			} else holder.responses.visibility = View.GONE

			if (work.votersCount != 0 && work.rating != null) {

				holder.rating.text = String.format("%s / %s",
						numberFormat.format(work.rating),
						numberFormat.format(work.votersCount))
				holder.rating.visibility = View.VISIBLE
			} else {
				holder.rating.visibility = View.GONE
			}
		val myMark = work.mark
		if (myMark != null || myMark == 0) {
			holder.mark.text = myMark.toString()
			holder.mark.visibility = View.VISIBLE
		} else holder.mark.visibility = View.GONE
	}

	inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView){
		var title: TextView = rootView.findViewById<View>(R.id.title) as FontTextView
		var authors: TextView = rootView.findViewById<View>(R.id.authors) as FontTextView
		var description: TextView = rootView.findViewById<View>(R.id.description) as FontTextView
		var year: TextView = rootView.findViewById<View>(R.id.year) as FontTextView
		var rating: TextView = rootView.findViewById<View>(R.id.rating) as FontTextView
		var responses: TextView = rootView.findViewById<View>(R.id.responses) as FontTextView
		var mark: TextView = rootView.findViewById<View>(R.id.mark) as FontTextView
	}
}
