package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.ViewGroup
import android.view.View
import ru.fantlab.android.R
import kotlinx.android.synthetic.main.bookcase_film_row_item.view.*
import ru.fantlab.android.data.dao.model.BookcaseFilm
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class BookcaseFilmViewHolder(itemView: View, adapter: BaseRecyclerAdapter<BookcaseFilm, BookcaseFilmViewHolder>)
    : BaseViewHolder<BookcaseFilm>(itemView, adapter) {

    override fun bind(bookcase: BookcaseFilm) {
        itemView.coverLayout.setUrl(Uri.Builder().scheme(LinkParserHelper.PROTOCOL_HTTPS)
                .authority(LinkParserHelper.HOST_DATA)
                .appendPath("images")
                .appendPath("films")
                .appendPath("big")
                .appendPath(bookcase.filmId.toString())
                .toString())

        itemView.filmAutors.text = bookcase.director
        itemView.filmName.text = bookcase.name
    }

    companion object {

        fun newInstance(
                viewGroup: ViewGroup,
                adapter: BaseRecyclerAdapter<BookcaseFilm, BookcaseFilmViewHolder>
        ): BookcaseFilmViewHolder =
                BookcaseFilmViewHolder(getView(viewGroup, R.layout.bookcase_film_row_item), adapter)
    }
}