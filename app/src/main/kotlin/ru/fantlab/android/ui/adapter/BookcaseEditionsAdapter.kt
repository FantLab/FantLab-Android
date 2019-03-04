package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.BookcaseEdition
import ru.fantlab.android.ui.adapter.viewholder.BookcaseEditionViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class BookcaseEditionsAdapter(nom: ArrayList<BookcaseEdition>?)
    : BaseRecyclerAdapter<BookcaseEdition, BookcaseEditionViewHolder>(nom!!) {

    override fun viewHolder(parent: ViewGroup, viewType: Int): BookcaseEditionViewHolder =
            BookcaseEditionViewHolder.newInstance(parent, this)

    override fun onBindView(holder: BookcaseEditionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

