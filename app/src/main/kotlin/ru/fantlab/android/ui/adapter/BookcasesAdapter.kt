package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.ui.adapter.viewholder.BookcaseViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class BookcasesAdapter(nom: ArrayList<Bookcase>?)
    : BaseRecyclerAdapter<Bookcase, BookcaseViewHolder>(nom!!) {

    override fun viewHolder(parent: ViewGroup, viewType: Int): BookcaseViewHolder =
            BookcaseViewHolder.newInstance(parent, this)

    override fun onBindView(holder: BookcaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
