package ru.fantlab.android.ui.adapter.viewholder

import ru.fantlab.android.data.dao.model.Bookcase
import android.view.View
import android.view.ViewGroup
import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class BookcaseViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Bookcase, BookcaseViewHolder, *>)
    : BaseViewHolder<Bookcase>(itemView, adapter) {

    override fun bind(bookcase: Bookcase) {
    }

    companion object {
        fun newInstance(
                viewGroup: ViewGroup,
                adapter: BaseRecyclerAdapter<Bookcase, BookcaseViewHolder, *>
        ): BookcaseViewHolder {
            return BookcaseViewHolder(getView(viewGroup, R.layout.response_row_item), adapter)
        }
    }
}