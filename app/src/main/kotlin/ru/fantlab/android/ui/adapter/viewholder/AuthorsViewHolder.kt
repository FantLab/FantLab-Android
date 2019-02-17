package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.authors_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.AuthorInList
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class AuthorsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<AuthorInList, AuthorsViewHolder>)
	: BaseViewHolder<AuthorInList>(itemView, adapter) {

	override fun bind(author: AuthorInList) {
		itemView.avatarLayout.setUrl(Uri.Builder().scheme(LinkParserHelper.PROTOCOL_HTTPS)
				.authority(LinkParserHelper.HOST_DATA)
				.appendPath("images")
				.appendPath("autors")
				.appendPath(author.id.toString())
				.toString())

		itemView.name.text = if (author.nameShort.isNotEmpty()) {
			author.nameShort
		} else {
			author.nameOrig
		}

		if (author.nameOrig.isNotEmpty()) {
			itemView.name_orig.text = author.nameOrig
			itemView.name_orig.visibility = View.VISIBLE
		} else {
			itemView.name_orig.visibility = View.GONE
		}
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<AuthorInList, AuthorsViewHolder>
		): AuthorsViewHolder =
				AuthorsViewHolder(getView(viewGroup, R.layout.authors_row_item), adapter)
	}
}