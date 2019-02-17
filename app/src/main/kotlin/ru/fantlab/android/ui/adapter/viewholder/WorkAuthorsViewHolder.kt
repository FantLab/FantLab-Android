package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.author_row_item.view.*
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Work
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class WorkAuthorsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Work.Author, WorkAuthorsViewHolder>)
	: BaseViewHolder<Work.Author>(itemView, adapter) {

	override fun bind(author: Work.Author) {
		itemView.avatarLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/autors/${author.id}")
		itemView.name.text = author.name
		if (!InputHelper.isEmpty(author.nameOrig))
			itemView.name_orig.text = author.nameOrig
		else
			itemView.name_orig.visibility = View.GONE

		itemView.setOnClickListener {
			AuthorPagerActivity.startActivity(App.instance, author.id, author.name, 0)
		}
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Work.Author, WorkAuthorsViewHolder>
		): WorkAuthorsViewHolder =
				WorkAuthorsViewHolder(getView(viewGroup, R.layout.author_row_item), adapter)
	}
}