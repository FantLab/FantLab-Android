package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.ui.adapter.viewholder.NewsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import java.util.*

class NewsAdapter @JvmOverloads constructor(events: ArrayList<News>, private val noImage: Boolean = false)
	: BaseRecyclerAdapter<News, NewsViewHolder, BaseViewHolder.OnItemClickListener<News>>(events) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
		return NewsViewHolder(NewsViewHolder.getView(parent, noImage), this)
	}

	override fun onBindView(holder: NewsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}
