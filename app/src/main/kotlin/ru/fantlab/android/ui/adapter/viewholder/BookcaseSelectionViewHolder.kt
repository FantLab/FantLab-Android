package ru.fantlab.android.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.View
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.BookcaseSelection
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.bookcase_selector_row_item.view.*
import ru.fantlab.android.ui.adapter.BookcaseSelectorAdapter

class BookcaseSelectionViewHolder(itemView: View, adapter: BookcaseSelectorAdapter)
    : BaseViewHolder<BookcaseSelection>(itemView, adapter) {

    override fun bind(bookcase: BookcaseSelection) {
        itemView.bookcaseIncluded.text = bookcase.bookcase.bookcaseName
        itemView.bookcaseIncluded.isChecked = bookcase.included
        itemView.bookcaseIncluded.setOnClickListener{
            if (adapter != null && (adapter as BookcaseSelectorAdapter).selectionListener != null) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && position < adapter!!.getItemCount()) {
                    (adapter as BookcaseSelectorAdapter).selectionListener?.onItemSelected(position, it, adapter?.getItem(position)!!)
                }
            }
        }
        itemView.bookcaseEdit.setOnClickListener { onClick(itemView) }
    }

    interface OnItemSelectListener<T> {
        fun onItemSelected(position: Int, v: View?, item: T)
    }

    companion object {

        fun newInstance(
                viewGroup: ViewGroup,
                adapter: BookcaseSelectorAdapter
        ): BookcaseSelectionViewHolder =
                BookcaseSelectionViewHolder(getView(viewGroup, R.layout.bookcase_selector_row_item), adapter)
    }
}