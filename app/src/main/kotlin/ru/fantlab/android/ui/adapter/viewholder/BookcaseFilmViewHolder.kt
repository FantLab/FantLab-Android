package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.ViewGroup
import android.view.View
import ru.fantlab.android.R
import kotlinx.android.synthetic.main.bookcase_film_row_item.view.*
import ru.fantlab.android.data.dao.model.BookcaseFilm
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.adapter.BookcaseFilmsAdapter
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
        itemView.workComment.text = if (bookcase.comment.orEmpty().isEmpty()) itemView.context.getString(R.string.bookcase_add_comment) else bookcase.comment
        itemView.workComment.setOnClickListener {
            if (adapter != null && (adapter as BookcaseFilmsAdapter).itemCommentUpdateListener != null) {
                (adapter as BookcaseFilmsAdapter).itemCommentUpdateListener?.onUpdateItemComment(bookcase.filmId, bookcase.comment)
            }
        }
        itemView.workDelete.setOnClickListener {
            if (adapter != null && (adapter as BookcaseFilmsAdapter).itemDeletionListener != null) {
                (adapter as BookcaseFilmsAdapter).itemDeletionListener?.onDeleteItemFromBookcase(bookcase.filmId)
            }
        }
    }

    interface OnDeleteItemFromBookcaseListener {
        fun onDeleteItemFromBookcase(itemId: Int)
    }

    interface OnUpdateItemCommentListener {
        fun onUpdateItemComment(itemId: Int, itemComment: String?)
    }

    companion object {

        fun newInstance(
                viewGroup: ViewGroup,
                adapter: BaseRecyclerAdapter<BookcaseFilm, BookcaseFilmViewHolder>
        ): BookcaseFilmViewHolder =
                BookcaseFilmViewHolder(getView(viewGroup, R.layout.bookcase_film_row_item), adapter)
    }
}