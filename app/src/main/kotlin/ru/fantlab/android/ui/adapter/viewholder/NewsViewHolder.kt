package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.news_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.provider.scheme.LinkParserHelper
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class NewsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<News, NewsViewHolder>)
	: BaseViewHolder<News>(itemView, adapter) {

	override fun bind(news: News) {
		val authorId = "user(\\d+)\\D".toRegex().find(news.author)
		itemView.avatarLayout.setUrl("https://${LinkParserHelper.HOST_DATA}/images/users/${authorId?.groupValues?.get(1)}")

		itemView.coverLayout.setUrl("https:${news.image}", "file:///android_asset/svg/fl_news.svg")

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
		itemView.author.text = news.author.replace("<.*>(.*?)<.*>".toRegex(), "$1")

		itemView.userInfo.setOnClickListener { listener?.onOpenContextMenu(news) }
	}

	interface OnOpenContextMenu {
		fun onOpenContextMenu(news: News)
	}

	companion object {
		private var listener: OnOpenContextMenu? = null

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<News, NewsViewHolder>
		): NewsViewHolder {
			return NewsViewHolder(getView(viewGroup, R.layout.news_row_item), adapter)
		}

		fun setOnContextMenuListener(listener: NewsViewHolder.OnOpenContextMenu) {
			this.listener = listener
		}

	}
}