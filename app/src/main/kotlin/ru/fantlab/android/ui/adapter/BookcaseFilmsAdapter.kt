package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.BookcaseFilm
import ru.fantlab.android.ui.adapter.viewholder.BookcaseFilmViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter

class BookcaseFilmsAdapter(nom: ArrayList<BookcaseFilm>?)
    : BaseRecyclerAdapter<BookcaseFilm, BookcaseFilmViewHolder>(nom!!) {

    override fun viewHolder(parent: ViewGroup, viewType: Int): BookcaseFilmViewHolder =
            BookcaseFilmViewHolder.newInstance(parent, this)

    override fun onBindView(holder: BookcaseFilmViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}