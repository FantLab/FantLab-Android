package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.fantlab.android.R
import kotlinx.android.synthetic.main.bookcase_work_row_item.view.*
import ru.fantlab.android.data.dao.model.BookcaseWork
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.adapter.BookcaseWorksAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class BookcaseWorkViewHolder(itemView: View, adapter: BaseRecyclerAdapter<BookcaseWork, BookcaseWorkViewHolder>)
    : BaseViewHolder<BookcaseWork>(itemView, adapter) {

    override fun bind(bookcase: BookcaseWork) {
        itemView.coverLayout.setUrl(Uri.Builder().scheme(LinkParserHelper.PROTOCOL_HTTPS)
                .authority(LinkParserHelper.HOST_DATA)
                .appendPath("images")
                .appendPath("works")
                .appendPath("big")
                .appendPath(bookcase.itemId.toString())
                .toString())

        itemView.workAutors.text = bookcase.autors
        itemView.workName.text = bookcase.name
        itemView.workComment.text = if (bookcase.comment.orEmpty().isEmpty()) itemView.context.getString(R.string.bookcase_add_comment) else bookcase.comment
        itemView.workComment.setOnClickListener {
            if (adapter != null && (adapter as BookcaseWorksAdapter).itemUpdateListener != null) {
                (adapter as BookcaseWorksAdapter).itemUpdateListener?.onUpdateItemComment(bookcase.itemId, bookcase.comment)
            }
        }
        itemView.workDelete.setOnClickListener {
            if (adapter != null && (adapter as BookcaseWorksAdapter).itemUpdateListener != null) {
                (adapter as BookcaseWorksAdapter).itemUpdateListener?.onDeleteItemFromBookcase(bookcase.itemId)
            }
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)

        if (adapter != null && (adapter as BookcaseWorksAdapter).itemUpdateListener != null) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION && position < adapter!!.getItemCount()) {
                (adapter as BookcaseWorksAdapter).itemUpdateListener?.onWorkClicked(adapter?.getItem(position)!!)
            }
        }
    }

    interface onUpdateItemListener {
        fun onDeleteItemFromBookcase(itemId: Int)

        fun onUpdateItemComment(itemId: Int, itemComment: String?)

        fun onWorkClicked(bookcase: BookcaseWork)
    }

    companion object {

        fun newInstance(
                viewGroup: ViewGroup,
                adapter: BaseRecyclerAdapter<BookcaseWork, BookcaseWorkViewHolder>
        ): BookcaseWorkViewHolder =
                BookcaseWorkViewHolder(getView(viewGroup, R.layout.bookcase_work_row_item), adapter)
    }
}