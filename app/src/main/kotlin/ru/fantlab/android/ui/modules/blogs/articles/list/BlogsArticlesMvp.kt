package ru.fantlab.android.ui.modules.blogs.articles.list

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.BlogArticles
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface BlogsArticlesMvp {

	interface View : BaseMvp.View,
			android.view.View.OnClickListener,
			SwipeRefreshLayout.OnRefreshListener {

		fun getLoadMore(): OnLoadMore<Int>

		fun onNotifyAdapter(articles: ArrayList<BlogArticles.Article>, page: Int)

		fun onItemClicked(item: BlogArticles.Article)

		fun onItemLongClicked(position: Int, v: android.view.View?, item: BlogArticles.Article)

	}

	interface Presenter : BaseMvp.Presenter,
			BaseMvp.PaginationListener<Int>,
			BaseViewHolder.OnItemClickListener<BlogArticles.Article>  {

		fun getArticles(force: Boolean)

	}
}