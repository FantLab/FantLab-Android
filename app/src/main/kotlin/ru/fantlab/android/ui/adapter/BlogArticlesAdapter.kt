package ru.fantlab.android.ui.adapter

import android.view.ViewGroup
import ru.fantlab.android.data.dao.model.BlogArticles
import ru.fantlab.android.ui.adapter.viewholder.BlogArticlesViewHolder
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import java.util.*

class BlogArticlesAdapter constructor(blogs: ArrayList<BlogArticles.Article>)
	: BaseRecyclerAdapter<BlogArticles.Article, BlogArticlesViewHolder>(blogs) {

	override fun viewHolder(parent: ViewGroup, viewType: Int): BlogArticlesViewHolder =
			BlogArticlesViewHolder.newInstance(parent, this)

	override fun onBindView(holder: BlogArticlesViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	fun setOnContextMenuListener(listener: BlogArticlesViewHolder.ArticleMenu) {
		BlogArticlesViewHolder.setOnArticleMenuListener(listener)
	}

}