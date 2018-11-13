package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.AuthorInList
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class AuthorsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<AuthorInList, AuthorsViewHolder, *>)
	: BaseViewHolder<AuthorInList>(itemView, adapter) {

	@BindView(R.id.avatarLayout) lateinit var avatarLayout: AvatarLayout
	@BindView(R.id.name) lateinit var name: FontTextView
	@BindView(R.id.name_orig) lateinit var nameOrig: FontTextView

	override fun bind(author: AuthorInList) {
		avatarLayout.setUrl(Uri.Builder().scheme(LinkParserHelper.PROTOCOL_HTTPS)
				.authority(LinkParserHelper.HOST_DATA)
				.appendPath("images")
				.appendPath("autors")
				.appendPath(author.id.toString())
				.toString())

		name.text = if (author.nameShort.isNotEmpty()) {
			author.nameShort
		} else {
			author.nameOrig
		}

		if (author.nameOrig.isNotEmpty()) {
			nameOrig.text = author.nameOrig
			nameOrig.visibility = View.VISIBLE
		} else {
			nameOrig.visibility = View.GONE
		}
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<AuthorInList, AuthorsViewHolder, *>
		): AuthorsViewHolder =
				AuthorsViewHolder(getView(viewGroup, R.layout.authors_row_item), adapter)
	}
}