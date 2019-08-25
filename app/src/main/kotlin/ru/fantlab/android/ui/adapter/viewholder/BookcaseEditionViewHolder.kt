package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import ru.fantlab.android.R
import kotlinx.android.synthetic.main.bookcase_edition_row_item.view.*
import ru.fantlab.android.data.dao.model.BookcaseEdition
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.adapter.BookcaseEditionsAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class BookcaseEditionViewHolder(itemView: View, adapter: BaseRecyclerAdapter<BookcaseEdition, BookcaseEditionViewHolder>)
    : BaseViewHolder<BookcaseEdition>(itemView, adapter) {

    override fun bind(bookcase: BookcaseEdition) {
        itemView.coverLayout.setUrl(Uri.Builder().scheme(LinkParserHelper.PROTOCOL_HTTPS)
                .authority(LinkParserHelper.HOST_DATA)
                .appendPath("images")
                .appendPath("editions")
                .appendPath("big")
                .appendPath(bookcase.editionId.toString())
                .toString())

        itemView.editionAutors.text = bookcase.autors
        itemView.editionName.text = bookcase.name
        itemView.editionPublishers.text = bookcase.publisher
        itemView.workComment.text = if (InputHelper.isEmpty(bookcase.comment)) itemView.context.getString(R.string.bookcase_add_comment) else bookcase.comment
        itemView.workComment.setOnClickListener {
            if (adapter != null && (adapter as BookcaseEditionsAdapter).itemUpdateListener != null) {
                (adapter as BookcaseEditionsAdapter).itemUpdateListener?.onUpdateItemComment(bookcase.editionId, bookcase.comment)
            }
        }
        itemView.workDelete.setOnClickListener {
            if (adapter != null && (adapter as BookcaseEditionsAdapter).itemUpdateListener != null) {
                (adapter as BookcaseEditionsAdapter).itemUpdateListener?.onDeleteItemFromBookcase(bookcase.editionId)
            }
        }
    }

    interface onUpdateItemListener {
        fun onDeleteItemFromBookcase(itemId: Int)

        fun onUpdateItemComment(itemId: Int, itemComment: String?)
    }


    companion object {

        fun newInstance(
                viewGroup: ViewGroup,
                adapter: BaseRecyclerAdapter<BookcaseEdition, BookcaseEditionViewHolder>
        ): BookcaseEditionViewHolder =
                BookcaseEditionViewHolder(getView(viewGroup, R.layout.bookcase_edition_row_item), adapter)
    }
}