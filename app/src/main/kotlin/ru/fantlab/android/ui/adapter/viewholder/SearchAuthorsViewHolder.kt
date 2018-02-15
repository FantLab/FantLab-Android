package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.SearchAuthorModel
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.widgets.AvatarLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.text.NumberFormat

class SearchAuthorsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<SearchAuthorModel, SearchAuthorsViewHolder, *>)
	: BaseViewHolder<SearchAuthorModel>(itemView, adapter) {

	@BindView(R.id.avatarLayout)
	lateinit var avatarLayout: AvatarLayout

	@BindView(R.id.name)
	lateinit var name: FontTextView

	@BindView(R.id.pseudo_names)
	lateinit var pseudoNames: FontTextView

	@BindView(R.id.country)
	lateinit var country: FontTextView

	@BindView(R.id.dates)
	lateinit var dates: FontTextView

	@BindView(R.id.rating)
	lateinit var rating: FontTextView

	@BindView(R.id.responses)
	lateinit var responses: FontTextView

	@BindView(R.id.editions)
	lateinit var editions: FontTextView

	@BindView(R.id.movies)
	lateinit var movies: FontTextView

	private val numberFormat = NumberFormat.getNumberInstance()

	override fun bind(author: SearchAuthorModel) {
		avatarLayout.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("autors")
				.appendPath(author.authorId.toString())
				.toString())

		name.text = if (author.rusName.isNotEmpty()) {
			if (author.name.isNotEmpty()) {
				String.format("%s / %s", author.rusName, author.name)
			} else {
				author.rusName
			}
		} else {
			author.name
		}

		if (author.pseudoNames.isNotEmpty()) {
			pseudoNames.text = author.pseudoNames
			pseudoNames.visibility = View.VISIBLE
		} else {
			pseudoNames.visibility = View.GONE
		}

		country.text = if (author.country.isNotEmpty()) author.country else "N/A"

		dates.text = StringBuilder()
				.append(if (author.birthYear != 0) author.birthYear else "N/A")
				.append(if (author.deathYear != 0) " - ${author.deathYear}" else "")

		if (author.markCount != 0) {
			rating.text = String.format("%s / %s",
					numberFormat.format(author.midMark / 100.0),
					numberFormat.format(author.markCount.toLong()))
			rating.visibility = View.VISIBLE
		} else {
			rating.visibility = View.GONE
		}

		if (author.responseCount != 0) {
			responses.text = numberFormat.format(author.responseCount.toLong())
			responses.visibility = View.VISIBLE
		} else {
			responses.visibility = View.GONE
		}

		if (author.editionCount != 0) {
			editions.text = numberFormat.format(author.editionCount.toLong())
			editions.visibility = View.VISIBLE
		} else {
			editions.visibility = View.GONE
		}

		if (author.movieCount != 0) {
			movies.text = numberFormat.format(author.movieCount.toLong())
			movies.visibility = View.VISIBLE
		} else {
			movies.visibility = View.GONE
		}
	}

	companion object {

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<SearchAuthorModel, SearchAuthorsViewHolder, *>) : SearchAuthorsViewHolder
				= SearchAuthorsViewHolder(getView(viewGroup, R.layout.search_authors_row_item), adapter)
	}
}