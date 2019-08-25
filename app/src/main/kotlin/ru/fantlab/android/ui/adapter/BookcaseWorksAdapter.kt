package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.BookcaseWork
import ru.fantlab.android.ui.adapter.viewholder.BookcaseWorkViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter

class BookcaseWorksAdapter(nom: ArrayList<BookcaseWork>,
                           var itemUpdateListener: BookcaseWorkViewHolder.onUpdateItemListener? = null)
    : BaseRecyclerAdapter<BookcaseWork, BookcaseWorkViewHolder>(nom) {

    override fun viewHolder(parent: ViewGroup, viewType: Int): BookcaseWorkViewHolder =
            BookcaseWorkViewHolder.newInstance(parent, this)

    override fun onBindView(holder: BookcaseWorkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}