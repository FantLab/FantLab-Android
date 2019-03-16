package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.ui.adapter.viewholder.NewsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class NewsAdapter constructor(news: ArrayList<News>)
	: BaseRecyclerAdapter<News, NewsViewHolder>(news) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
			NewsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: NewsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	fun setOnContextMenuListener(listener: NewsViewHolder.OnOpenContextMenu) {
		NewsViewHolder.setOnContextMenuListener(listener)
	}

}