package ru.fantlab.android.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.blogs_row_item.view.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Blogs
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.getTimeAgo
import ru.fantlab.android.helper.parseFullDate
import ru.fantlab.android.ui.widgets.recyclerview.BaseRecyclerAdapter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class BlogsViewHolder(itemView: View, adapter: BaseRecyclerAdapter<Blogs.Blog, BlogsViewHolder>)
	: BaseViewHolder<Blogs.Blog>(itemView, adapter) {

	override fun bind(blog: Blogs.Blog) {
		Glide.with(itemView.context).load(blog.user.avatar).into(itemView.userAvatar)

		itemView.blogUsername.text = if (!InputHelper.isEmpty(blog.user.name) && !InputHelper.isEmpty(blog.user.login))
			"${blog.user.login} (${blog.user.name})" else blog.user.login

		itemView.articleCount.text = blog.stats.articleCount.toString()
		itemView.subscriberCount.text = blog.stats.subscriberCount.toString()
		itemView.lastDate.text = blog.lastArticle.date.parseFullDate(true).getTimeAgo()
		if (blog.lastArticle.title != null) itemView.lastArticleTitle.text = blog.lastArticle.title else itemView.lastArticleTitle.visibility = View.GONE
	}

	interface MessageMenu {
		fun onOpenContextMenu(topicMessage: Blogs.Blog)
	}

	companion object {
		private var listener: MessageMenu? = null

		fun newInstance(
				viewGroup: ViewGroup,
				adapter: BaseRecyclerAdapter<Blogs.Blog, BlogsViewHolder>
		): BlogsViewHolder {
			return BlogsViewHolder(getView(viewGroup, R.layout.blogs_row_item), adapter)
		}

		fun setOnMessageMenuListener(listener: MessageMenu) {
			this.listener = listener
		}


	}
}