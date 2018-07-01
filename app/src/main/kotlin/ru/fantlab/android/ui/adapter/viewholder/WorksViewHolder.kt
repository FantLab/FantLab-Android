package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class WorksViewHolder(itemView: View, adapter: BaseRecyclerAdapter<WorksBlocks.Work, WorksViewHolder, *>)
	: BaseViewHolder<WorksBlocks.Work>(itemView, adapter) {

	@BindView(R.id.coverLayout) lateinit var coverLayout: CoverLayout
	@BindView(R.id.authors) lateinit var authors: FontTextView
	@BindView(R.id.title) lateinit var title: FontTextView
	@BindView(R.id.year) lateinit var year: FontTextView

	override fun bind(work: WorksBlocks.Work) {
		/*

		Нет ссылки в API на верное изображение

		coverLayout.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("editions")
				.appendPath("big")
				.appendPath(work.id.toString())
				.toString())

		*/

		if (work.authors.isNotEmpty()) {
			authors.text = work.authors[0].name.replace(ANY_CHARACTERS_IN_BRACKETS_REGEX, "")
			authors.visibility = View.VISIBLE
		} else {
			authors.visibility = View.GONE
		}

        title.text = if (work.name.isNotEmpty()) {
            if (work.nameOrig.isNotEmpty()) {
                String.format("%s / %s", work.nameOrig, work.name)
            } else {
                work.name
            }
        } else {
            work.nameOrig
        }

		year.text = if (work.year != null) work.year.toString() else "N/A"

	}

	companion object {

		private val ANY_CHARACTERS_IN_BRACKETS_REGEX = "\\[.*?]".toRegex()

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<WorksBlocks.Work, WorksViewHolder, *>) : WorksViewHolder
				= WorksViewHolder(getView(viewGroup, R.layout.work_row_item), adapter)
	}
}