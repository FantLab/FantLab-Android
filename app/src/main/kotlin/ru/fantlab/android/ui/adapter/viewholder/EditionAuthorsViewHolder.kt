package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.author_row_item.view.*
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Edition
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.modules.author.AuthorPagerActivity
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class EditionAuthorsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Edition.Author, EditionAuthorsViewHolder>)
	: BaseViewHolder<Edition.Author>(itemView, adapter) {

	override fun bind(author: Edition.Author) {
		itemView.authorAvatar.setUrl("https://${LinkParserHelper.HOST_DATA}/images/autors/${author.id}")

		if (!InputHelper.isEmpty(author.name)){
			itemView.authorName.text = author.name
			itemView.authorName.visibility = View.VISIBLE
		} else itemView.authorName.visibility = View.GONE

		itemView.authorOrigName.visibility = View.GONE

		itemView.setOnClickListener {
			AuthorPagerActivity.startActivity(App.instance, author.id, author.name, 0)
		}
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Edition.Author, EditionAuthorsViewHolder>
		): EditionAuthorsViewHolder =
				EditionAuthorsViewHolder(getView(viewGroup, R.layout.author_row_item), adapter)
	}
}