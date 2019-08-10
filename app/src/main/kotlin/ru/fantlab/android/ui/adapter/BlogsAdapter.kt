package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.Blogs
import ru.fantlab.android.ui.adapter.viewholder.BlogsViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class BlogsAdapter constructor(blogs: ArrayList<Blogs.Blog>)
	: BaseRecyclerAdapter<Blogs.Blog, BlogsViewHolder>(blogs) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): BlogsViewHolder =
			BlogsViewHolder.newInstance(parent, this)

	override fun onBindView(holder: BlogsViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	fun setOnContextMenuListener(listener: BlogsViewHolder.MessageMenu) {
		BlogsViewHolder.setOnMessageMenuListener(listener)
	}

}