package ru.fantlab.android.ui.modules.blogs.main

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.Keep
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.ContextMenuBuilder
import ru.fantlab.android.data.dao.model.Blogs
import ru.fantlab.android.data.dao.model.ContextMenus
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.BlogsAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.blogs.BlogsMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView
import java.util.*

class BlogsMainFragment : BaseFragment<BlogsMainMvp.View, BlogsMainPresenter>(),
		BlogsMainMvp.View {

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = BlogsMainPresenter()

	private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter) }

	private var blogsCallback: BlogsMvp.View? = null

	private val adapter: BlogsAdapter by lazy { BlogsAdapter(arrayListOf()) }

	override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
		blogsCallback?.setTitle(getString(R.string.blogs_list))

		stateLayout.setEmptyText(R.string.no_blogs)
		stateLayout.setOnReloadListener(this)
		refresh.setOnRefreshListener(this)
		recycler.setEmptyView(stateLayout, refresh)
		adapter.listener = presenter
		//adapter.setOnContextMenuListener(this)
		recycler.adapter = adapter

		getLoadMore().initialize(presenter.getCurrentPage() - 1, presenter.getPreviousTotal())
		recycler.addOnScrollListener(getLoadMore())
		presenter.onCallApi(1, null)
		fastScroller.attachRecyclerView(recycler)
		setHasOptionsMenu(true)
	}

	override fun onHiddenChanged(hidden: Boolean) {
		super.onHiddenChanged(hidden)
		if (!hidden) blogsCallback?.setTitle(getString(R.string.blogs_list))
	}

	override fun getLoadMore() = onLoadMore

	override fun onNotifyAdapter(blogs: ArrayList<Blogs.Blog>, page: Int) {
		hideProgress()
		if (blogs.isEmpty()) {
			adapter.clear()
			stateLayout.showEmptyState()
			return
		}
		if (page <= 1) {
			adapter.insertItems(blogs)
		} else {
			adapter.addItems(blogs)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.blogs_menu, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.sort -> {
				showSortDialog()
			}
		}
		return super.onOptionsItemSelected(item)
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		refresh.isRefreshing = true
		stateLayout.showProgress()
	}

	override fun hideProgress() {
		refresh.isRefreshing = false
		stateLayout.hideProgress()
	}

	override fun showErrorMessage(msgRes: String?) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(titleRes: Int, msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	override fun onClick(v: View?) {
		onRefresh()
	}

	override fun onRefresh() {
		presenter.onCallApi(1, null)
	}

	fun showSortDialog() {
		val dialogView = ContextMenuDialogView()
		val sort = presenter.getCurrentSort()
		dialogView.initArguments("main", ContextMenuBuilder.buildForBlogsSorting(recycler.context, sort))
		dialogView.show(childFragmentManager, "ContextMenuDialogView")
	}

	override fun onItemSelected(parent: String, item: ContextMenus.MenuItem, position: Int, listItem: Any) {
		presenter.setCurrentSort(item.id)
	}

	override fun onItemClicked(item: Blogs.Blog) {
	}

	override fun onItemLongClicked(position: Int, v: View?, item: Blogs.Blog) {
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is BlogsMvp.View) {
			blogsCallback = context
		}
	}

	override fun onDetach() {
		blogsCallback = null
		super.onDetach()
	}

	companion object {
		@Keep
		val TAG: String = BlogsMainFragment::class.java.simpleName
	}

}