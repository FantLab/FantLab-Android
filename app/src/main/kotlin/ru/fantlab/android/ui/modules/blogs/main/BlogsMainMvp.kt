package ru.fantlab.android.ui.modules.blogs.main

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.fantlab.android.data.dao.model.Blogs
import ru.fantlab.android.provider.rest.BlogsSortOption
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface BlogsMainMvp {

	interface View : BaseMvp.View,
			android.view.View.OnClickListener,
			SwipeRefreshLayout.OnRefreshListener,
			ContextMenuDialogView.ListDialogViewActionCallback {

		fun getLoadMore(): OnLoadMore<Int>

		fun onNotifyAdapter(blogs: ArrayList<Blogs.Blog>, page: Int)

		fun onItemClicked(item: Blogs.Blog)

		fun onItemLongClicked(position: Int, v: android.view.View?, item: Blogs.Blog)

	}

	interface Presenter : BaseMvp.Presenter,
			BaseMvp.PaginationListener<Int>,
			BaseViewHolder.OnItemClickListener<Blogs.Blog>  {

		fun getBlogs(force: Boolean)

		fun setCurrentSort(sortValue: String)

		fun getCurrentSort(): BlogsSortOption
	}
}