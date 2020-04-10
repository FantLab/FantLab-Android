package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.blog_article_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.BlogArticles
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class BlogArticlesViewHolder(itemView: View, adapter: BaseRecyclerAdapter<BlogArticles.Article, BlogArticlesViewHolder>)
	: BaseViewHolder<BlogArticles.Article>(itemView, adapter) {

	override fun bind(article: BlogArticles.Article) {
		itemView.userAvatar.setUrl(article.creation.user.avatar)
		itemView.blogUser.text = article.creation.user.login
		itemView.date.text = article.creation.date.parseFullDate(true).getTimeAgo()
		itemView.articleTitle.text = article.title
		itemView.comments.text = article.stats.commentCount ?: "0"
		itemView.votes.text = article.stats.likeCount ?: "0"
		itemView.views.text = article.stats.viewsCount ?: "0"
	}

	interface ArticleMenu {
		fun onOpenContextMenu(article: BlogArticles.Article)
	}

	companion object {
		private var listener: ArticleMenu? = null

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<BlogArticles.Article, BlogArticlesViewHolder>
		): BlogArticlesViewHolder {
			return BlogArticlesViewHolder(getView(viewGroup, R.layout.blog_article_row_item), adapter)
		}

		fun setOnArticleMenuListener(listener: ArticleMenu) {
			this.listener = listener
		}


	}
}