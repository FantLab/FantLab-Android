package ru.fantlab.android.ui.adapter.viewholder

import ru.fantlab.android.data.dao.model.Bookcase
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.ui.widgets.CoverLayout
import ru.fantlab.android.ui.widgets.FontTextView
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class BookcaseViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Bookcase, BookcaseViewHolder, *>)
    : BaseViewHolder<Bookcase>(itemView, adapter) {

    @JvmField @BindView(R.id.coverLayout) var coverLayout: CoverLayout? = null
    @BindView(R.id.bookcaseName) lateinit var bookcaseName: FontTextView
    @BindView(R.id.bookcaseDescription) lateinit var bookcaseDescription: FontTextView

    override fun bind(bookcase: Bookcase) {
        coverLayout?.setUrl(null)

        bookcaseName.text = bookcase.name

        bookcaseDescription.text = bookcase.description
                .replace("(\r\n)+".toRegex(), "\n")    // пустые переносы строк
                .replace("\\[spoiler].*|\\[\\/spoiler]".toRegex(), "")
                .replace("\\[.*]".toRegex(), "")       // bb-коды
                .replace(":\\w+:".toRegex(), "")       // смайлы

    }

    companion object {
        fun newInstance(
                viewGroup: ViewGroup,
                adapter: BaseRecyclerAdapter<Bookcase, BookcaseViewHolder, *>
        ): BookcaseViewHolder {
            return BookcaseViewHolder(getView(viewGroup, R.layout.bookcase_row_item), adapter)
        }
    }
}