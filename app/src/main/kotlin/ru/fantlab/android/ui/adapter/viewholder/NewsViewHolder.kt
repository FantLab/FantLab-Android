package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import kotlinx.android.synthetic.main.news_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder
import com.bumptech.glide.request.target.Target

class NewsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<News, NewsViewHolder>)
	: BaseViewHolder<News>(itemView, adapter) {

	override fun bind(news: News) {
		Glide.with(itemView.context).load("https:${news.image}")
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.listener(object : RequestListener<String, GlideDrawable> {
					override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
						itemView.coverLayout.visibility = View.GONE
						return false
					}
					override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
						itemView.coverLayout.visibility = View.VISIBLE
						return false
					}
				})
				.into(itemView.coverLayout)

		if (!InputHelper.isEmpty(news.title)) {
			itemView.title.text = news.title
			itemView.title.visibility = View.VISIBLE
		} else itemView.title.visibility = View.GONE

		if (!InputHelper.isEmpty(news.description) && !news.newsText.contains("\\[print_contest=(\\d+)\\]".toRegex())) {
			itemView.newsText.text = news.description.replace("<.*>(.*?)<\\/.*>".toRegex(), "$1")
			itemView.newsText.visibility = View.VISIBLE
		} else itemView.newsText.visibility = View.GONE

		itemView.category.text = news.category.capitalize()
		itemView.date.text = news.newsDateIso.parseFullDate(true).getTimeAgo()
		itemView.author.html = news.author
	}


	companion object {

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<News, NewsViewHolder>
		): NewsViewHolder {
			return NewsViewHolder(getView(viewGroup, R.layout.news_row_item), adapter)
		}

	}
}