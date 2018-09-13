package ru.fantlab.android.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.CycleWork
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.treeview.TreeNode
import ru.fantlab.android.ui.widgets.treeview.TreeViewAdapter
import ru.fantlab.android.ui.widgets.treeview.TreeViewBinder
import java.text.NumberFormat

class CycleWorkViewHolder : TreeViewBinder<CycleWorkViewHolder.ViewHolder>() {

	@BindView(R.id.authors) lateinit var authors: FontTextView
	@BindView(R.id.title) lateinit var title: FontTextView
	@BindView(R.id.description) lateinit var description: FontTextView
	@BindView(R.id.year) lateinit var year: FontTextView
	@BindView(R.id.rating) lateinit var rating: FontTextView
	@BindView(R.id.responses) lateinit var responses: FontTextView
	private val numberFormat = NumberFormat.getNumberInstance()

	override val layoutId = R.layout.author_cycle_work_row_item

	override fun provideViewHolder(itemView: View): ViewHolder {
		ButterKnife.bind(this, itemView)
		return ViewHolder(itemView)
	}

	override fun bindView(
			holder: RecyclerView.ViewHolder, position: Int, node: TreeNode<*>, onTreeNodeListener: TreeViewAdapter.OnTreeNodeListener?
	) {
		val work = (node.content as CycleWork).work
			if (work.authors.size > 1) {
				authors.text = work.authors.joinToString { it.name }
				authors.visibility = View.VISIBLE
			} else {
				authors.visibility = View.GONE
			}

			title.text = if (work.name.isNotEmpty()) {
				if (work.nameOrig.isNotEmpty()) {
					String.format("%s / %s", work.name, work.nameOrig)
				} else {
					work.name
				}
			} else {
				work.nameOrig
			}

			if (!InputHelper.isEmpty(work.description)) {
				description.text = work.description?.let { work.description.replace("\\[(.*?)]".toRegex(), "") }
			} else description.visibility = View.GONE

			if (!InputHelper.isEmpty(work.year)) {
				year.text = work.year.toString()
			} else year.visibility = View.GONE

			if (work.responses != "0") {
				responses.text = work.responses
				responses.visibility = View.VISIBLE
			} else responses.visibility = View.GONE

			if (work.votersCount != 0 && work.rating != null) {

				rating.text = String.format("%s / %s",
						numberFormat.format(work.rating),
						numberFormat.format(work.votersCount))
				rating.visibility = View.VISIBLE
			} else {
				rating.visibility = View.GONE
			}
	}

	inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView)
}
