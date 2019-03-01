package ru.fantlab.android.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.BookcaseEdition
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.Dot
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class BookcaseEditionViewHolder(itemView: View, adapter: BaseRecyclerAdapter<BookcaseEdition, BookcaseEditionViewHolder, *>)
    : BaseViewHolder<BookcaseEdition>(itemView, adapter) {

    @BindView(R.id.coverLayout) lateinit var coverLayout: CoverLayout
    @BindView(R.id.editionAutors) lateinit var bookcaseEditionAutors: FontTextView
    @BindView(R.id.editionName) lateinit var bookcaseEditionName: FontTextView
    @BindView(R.id.editionPublishers) lateinit var bookcaseEditionPublishers: FontTextView

    override fun bind(bookcase: BookcaseEdition) {
        coverLayout.setUrl(Uri.Builder().scheme(LinkParserHelper.PROTOCOL_HTTPS)
                .authority(LinkParserHelper.HOST_DATA)
                .appendPath("images")
                .appendPath("editions")
                .appendPath("big")
                .appendPath(bookcase.editionId.toString())
                .toString())

        bookcaseEditionAutors.text = bookcase.autors
        bookcaseEditionName.text = bookcase.name
        bookcaseEditionPublishers.text = bookcase.publisher
    }

    companion object {
        fun newInstance(
                viewGroup: ViewGroup,
                adapter: BaseRecyclerAdapter<BookcaseEdition, BookcaseEditionViewHolder, *>
        ): BookcaseEditionViewHolder {
            return BookcaseEditionViewHolder(getView(viewGroup, R.layout.bookcase_edition_row_item), adapter)
        }
    }
}