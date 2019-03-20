package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.ViewGroup
import android.view.View
import ru.fantlab.android.R
import kotlinx.android.synthetic.main.bookcase_work_row_item.view.*
import ru.fantlab.android.data.dao.model.BookcaseWork
import ru.fantlab.android.provider.scheme.LinkParserHelper
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
    }

    companion object {

        fun newInstance(
                viewGroup: ViewGroup,
                adapter: BaseRecyclerAdapter<BookcaseWork, BookcaseWorkViewHolder>
        ): BookcaseWorkViewHolder =
                BookcaseWorkViewHolder(getView(viewGroup, R.layout.bookcase_work_row_item), adapter)
    }
}