package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
            if (adapter != null && (adapter as BookcaseFilmsAdapter).itemUpdateListener != null) {
                (adapter as BookcaseFilmsAdapter).itemUpdateListener?.onUpdateItemComment(bookcase.filmId, bookcase.comment)
            }
        }
        itemView.workDelete.setOnClickListener {
            if (adapter != null && (adapter as BookcaseFilmsAdapter).itemUpdateListener != null) {
                (adapter as BookcaseFilmsAdapter).itemUpdateListener?.onDeleteItemFromBookcase(bookcase.filmId)
            }
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)

        if (adapter != null && (adapter as BookcaseFilmsAdapter).itemUpdateListener != null) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION && position < adapter!!.getItemCount()) {
                (adapter as BookcaseFilmsAdapter).itemUpdateListener?.onFilmClicked(adapter?.getItem(position)!!)
            }
        }
    }


    interface OnUpdateItemListener {
        fun onDeleteItemFromBookcase(itemId: Int)

        fun onUpdateItemComment(itemId: Int, itemComment: String?)

        fun onFilmClicked(bookcase: BookcaseFilm)
    }

    companion object {

        fun newInstance(
                viewGroup: ViewGroup,
                adapter: BaseRecyclerAdapter<BookcaseFilm, BookcaseFilmViewHolder>,
                isPrivateCase: Boolean
        ): BookcaseFilmViewHolder =
                BookcaseFilmViewHolder(getView(viewGroup, if (isPrivateCase) R.layout.bookcase_film_row_item else R.layout.public_bookcase_film_row_item), adapter)
    }
}