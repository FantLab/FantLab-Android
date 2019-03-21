package ru.fantlab.android.ui.adapter.viewholder

import android.view.ViewGroup
import android.view.View
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.BookcaseSelection
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.bookcase_selector_row_item.view.*

class BookcaseSelectionViewHolder(itemView: View, adapter: BaseRecyclerAdapter<BookcaseSelection, BookcaseSelectionViewHolder>)
    : BaseViewHolder<BookcaseSelection>(itemView, adapter) {

    override fun bind(bookcase: BookcaseSelection) {
        itemView.bookcaseName.text = bookcase.bookcase.name
        itemView.bookcaseIncluded.isChecked = bookcase.included
    }

    companion object {

        fun newInstance(
                viewGroup: ViewGroup,
                adapter: BaseRecyclerAdapter<BookcaseSelection, BookcaseSelectionViewHolder>
        ): BookcaseSelectionViewHolder =
                BookcaseSelectionViewHolder(getView(viewGroup, R.layout.bookcase_selector_row_item), adapter)
    }
}