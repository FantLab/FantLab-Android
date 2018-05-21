package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.newmodel.SearchWork
import ru.fantlab.android.provider.scheme.LinkParserHelper.HOST_DATA
import ru.fantlab.android.provider.scheme.LinkParserHelper.PROTOCOL_HTTPS
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.text.NumberFormat

class SearchWorksViewHolder(itemView: View, adapter: BaseRecyclerAdapter<SearchWork, SearchWorksViewHolder, *>)
	: BaseViewHolder<SearchWork>(itemView, adapter) {

	@BindView(R.id.coverLayout) lateinit var coverLayout: CoverLayout
	@BindView(R.id.authors) lateinit var authors: FontTextView
	@BindView(R.id.title) lateinit var title: FontTextView
	@BindView(R.id.year) lateinit var year: FontTextView
	@BindView(R.id.rating) lateinit var rating: FontTextView
	private val numberFormat = NumberFormat.getNumberInstance()

	override fun bind(work: SearchWork) {
		val coverId = if (work.pictureEditionIdAuto.isNotEmpty()) {
			work.pictureEditionIdAuto.toInt()
		} else {
			0 // no image
		}
		coverLayout.setUrl(Uri.Builder().scheme(PROTOCOL_HTTPS)
				.authority(HOST_DATA)
				.appendPath("images")
				.appendPath("editions")
				.appendPath("big")
				.appendPath(coverId.toString())
				.toString())

		val authorsText = when {
			// сначала пытаемся вычленить автора из названия (хак для журналов)
			// прим.: решение все равно покрывает не все возможные случаи из-за косяков в базе
			work.rusName.contains(AUTHORS_REGEX) -> {
				// произведение без отдельной/открытой страницы с несколькими авторами
				work.rusName.substring(work.rusName.lastIndexOf("Авторы", ignoreCase = true) + "Авторы".length + 1).trim()
			}
			work.rusName.contains(AUTHOR_REGEX) -> {
				// произведение без отдельной/открытой страницы с одним автором
				work.rusName.substring(work.rusName.lastIndexOf("Автор", ignoreCase = true) + "Автор".length + 1).trim()
			}
			work.name.contains(AUTHORS_REGEX) -> {
				// произведение без отдельной/открытой страницы и перевода с несколькими авторами
				work.name.substring(work.name.lastIndexOf("Авторы", ignoreCase = true) + "Авторы".length + 1).trim()
			}
			work.name.contains(AUTHOR_REGEX) -> {
				// произведение без отдельной/открытой страницы и перевода с одним автором
				work.name.substring(work.name.lastIndexOf("Автор", ignoreCase = true) + "Автор".length + 1).trim()
			}
			work.allAuthorRusName.isNotEmpty() -> work.allAuthorRusName
			work.allAuthorName.isNotEmpty() -> work.allAuthorName
			else -> ""
		}
		if (authorsText.isNotEmpty()) {
			authors.text = authorsText
			authors.visibility = View.VISIBLE
		} else {
			authors.visibility = View.GONE
		}

		val rusName = if (work.rusName.contains(AUTHORS_REGEX) || work.rusName.contains(AUTHOR_REGEX)) {
			work.rusName.substring(0, work.rusName.lastIndexOf("//")).trim()
		} else {
			work.rusName
		}
		val name = if (work.name.contains(AUTHORS_REGEX) || work.name.contains(AUTHOR_REGEX)) {
			work.name.substring(0, work.name.lastIndexOf("//")).trim()
		} else {
			work.name
		}
		title.text = if (rusName.isNotEmpty()) {
			if (name.isNotEmpty()) {
				String.format("%s / %s", rusName, name)
			} else {
				rusName
			}
		} else {
			name
		}

		year.text = if (work.year != 0) work.year.toString() else "N/A"

		if (work.markCount != 0) {
			rating.text = String.format("%s / %s",
					numberFormat.format(work.midMark[0].toDouble()),
					numberFormat.format(work.markCount.toLong()))
			rating.visibility = View.VISIBLE
		} else {
			rating.visibility = View.GONE
		}
	}

	companion object {

		private val AUTHORS_REGEX = "//\\s*Авторы:*".toRegex(RegexOption.IGNORE_CASE)
		private val AUTHOR_REGEX = "//\\s*Автор:*".toRegex(RegexOption.IGNORE_CASE)

		fun newInstance(viewGroup: ViewGroup, adapter: BaseRecyclerAdapter<SearchWork, SearchWorksViewHolder, *>): SearchWorksViewHolder
				= SearchWorksViewHolder(getView(viewGroup, R.layout.search_works_row_item), adapter)
	}
}