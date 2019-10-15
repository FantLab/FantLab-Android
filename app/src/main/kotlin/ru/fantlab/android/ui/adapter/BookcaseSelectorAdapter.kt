package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.BookcaseSelection
import ru.fantlab.android.ui.adapter.viewholder.BookcaseSelectionViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter

class BookcaseSelectorAdapter(nom: ArrayList<BookcaseSelection>, var selectionListener: BookcaseSelectionViewHolder.OnItemSelectListener<BookcaseSelection>? = null)
    : BaseRecyclerAdapter<BookcaseSelection, BookcaseSelectionViewHolder>(nom) {

    override fun viewHolder(parent: ViewGroup, viewType: Int): BookcaseSelectionViewHolder =
            BookcaseSelectionViewHolder.newInstance(parent, this)

    override fun onBindView(holder: BookcaseSelectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}