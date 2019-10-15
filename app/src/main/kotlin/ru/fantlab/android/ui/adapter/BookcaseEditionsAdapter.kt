package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.BookcaseEdition
import ru.fantlab.android.ui.adapter.viewholder.BookcaseEditionViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter

class BookcaseEditionsAdapter(nom: ArrayList<BookcaseEdition>,
                              var itemUpdateListener: BookcaseEditionViewHolder.OnUpdateItemListener? = null,
                              val isPrivateCase: Boolean)
    : BaseRecyclerAdapter<BookcaseEdition, BookcaseEditionViewHolder>(nom) {

    override fun viewHolder(parent: ViewGroup, viewType: Int): BookcaseEditionViewHolder =
            BookcaseEditionViewHolder.newInstance(parent, this, isPrivateCase)

    override fun onBindView(holder: BookcaseEditionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}