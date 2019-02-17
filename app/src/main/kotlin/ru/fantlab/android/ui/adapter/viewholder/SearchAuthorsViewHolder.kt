package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.search_authors_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.SearchAuthor
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.text.NumberFormat

class SearchAuthorsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<SearchAuthor, SearchAuthorsViewHolder>)
	: BaseViewHolder<SearchAuthor>(itemView, adapter) {

	private val numberFormat = NumberFormat.getNumberInstance()

	override fun bind(author: SearchAuthor) {
		itemView.avatarLayout.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("autors")
				.appendPath(author.authorId.toString())
				.toString())

		itemView.name.text = if (author.rusName.isNotEmpty()) {
			if (author.name.isNotEmpty()) {
				String.format("%s / %s", author.rusName, author.name)
			} else {
				author.rusName
			}
		} else {
			author.name
		}

		if (author.pseudoNames.isNotEmpty()) {
			itemView.pseudo_names.text = author.pseudoNames
			itemView.pseudo_names.visibility = View.VISIBLE
		} else {
			itemView.pseudo_names.visibility = View.GONE
		}


		if (author.country.isNotEmpty()) itemView.country.text = author.country else itemView.country.visibility = View.GONE

		if (author.birthYear != 0 || author.deathYear != 0) {
			itemView.dates.text = StringBuilder()
					.append(if (author.birthYear != 0) author.birthYear else "N/A")
					.append(if (author.deathYear != 0) " - ${author.deathYear}" else "")
		} else itemView.dates.visibility = View.GONE

		if (author.markCount != 0) {
			itemView.rating.text = String.format("%s / %s",
					numberFormat.format(author.midMark / 100.0),
					numberFormat.format(author.markCount.toLong()))
			itemView.rating.visibility = View.VISIBLE
		} else {
			itemView.rating.visibility = View.GONE
		}

		if (author.responseCount != 0) {
			itemView.responses.text = numberFormat.format(author.responseCount.toLong())
			itemView.responses.visibility = View.VISIBLE
		} else {
			itemView.responses.visibility = View.GONE
		}

		if (author.editionCount != 0) {
			itemView.editions.text = numberFormat.format(author.editionCount.toLong())
			itemView.editions.visibility = View.VISIBLE
		} else {
			itemView.editions.visibility = View.GONE
		}

		if (author.movieCount != 0) {
			itemView.movies.text = numberFormat.format(author.movieCount.toLong())
			itemView.movies.visibility = View.VISIBLE
		} else {
			itemView.movies.visibility = View.GONE
		}
	}

	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<SearchAuthor, SearchAuthorsViewHolder>
		): SearchAuthorsViewHolder =
				SearchAuthorsViewHolder(getView(viewGroup, R.layout.search_authors_row_item), adapter)
	}
}