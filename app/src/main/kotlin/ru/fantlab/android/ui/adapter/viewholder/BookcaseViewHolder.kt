package ru.fantlab.android.ui.adapter.viewholder

import ru.fantlab.android.data.dao.model.Bookcase
import android.view.View
import android.view.ViewGroup
import ru.fantlab.android.R
import kotlinx.android.synthetic.main.bookcase_row_item.view.*
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class BookcaseViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Bookcase, BookcaseViewHolder>)
    : BaseViewHolder<Bookcase>(itemView, adapter) {

    override fun bind(bookcase: Bookcase) {
        itemView.coverLayout.setUrl("http://www.fantlab.ru/img/bc_mybooks.gif")

        itemView.bookcaseName.text = bookcase.name

        itemView.bookcaseDescription.text = bookcase.description
                .replace("(\r\n)+".toRegex(), "\n")    // пустые переносы строк
                .replace("\\[spoiler].*|\\[\\/spoiler]".toRegex(), "")
                .replace("\\[.*]".toRegex(), "")       // bb-коды
                .replace(":\\w+:".toRegex(), "")       // смайлы

    }

    companion object {

        fun newInstance(
                viewGroup: ViewGroup,
                adapter: BaseRecyclerAdapter<Bookcase, BookcaseViewHolder>
        ): BookcaseViewHolder =
                BookcaseViewHolder(getView(viewGroup, R.layout.bookcase_row_item), adapter)
    }
}