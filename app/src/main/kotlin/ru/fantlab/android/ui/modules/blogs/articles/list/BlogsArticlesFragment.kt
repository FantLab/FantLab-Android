package ru.fantlab.android.ui.modules.blogs.articles.list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.micro_grid_refresh_list.*
import kotlinx.android.synthetic.main.state_layout.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.BlogArticles
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.provider.rest.loadmore.OnLoadMore
import ru.fantlab.android.ui.adapter.BlogArticlesAdapter
import ru.fantlab.android.ui.base.BaseFragment
import ru.fantlab.android.ui.modules.blogs.BlogsMvp
import ru.fantlab.android.ui.modules.blogs.articles.overview.BlogArticleOverviewActivity
import java.util.*

class BlogsArticlesFragment : BaseFragment<BlogsArticlesMvp.View, BlogsArticlesPresenter>(),
		BlogsArticlesMvp.View {

	override fun fragmentLayout() = R.layout.micro_grid_refresh_list

	override fun providePresenter() = BlogsArticlesPresenter()

	private val onLoadMore: OnLoadMore<Int> by lazy { OnLoadMore(presenter) }

	private var blogsCallback: BlogsMvp.View? = null

	private val adapter: BlogArticlesAdapter by lazy { BlogArticlesAdapter(arrayListOf()) }

	@State
	var blogId = -1
	@State
	var isClosed = false
	@State
	var blogTitle = ""

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

		blogId = arguments!!.getInt(BundleConstant.EXTRA)
		blogTitle = arguments!!.getString(BundleConstant.EXTRA_TWO) ?: getString(R.string.blogs_list)
		isClosed = arguments!!.getBoolean(BundleConstant.YES_NO_EXTRA)
		blogsCallback?.setTitle(blogTitle)

		presenter.onCallApi(1, blogId)
		fastScroller.attachRecyclerView(recycler)
		setHasOptionsMenu(true)

	}

	override fun onHiddenChanged(hidden: Boolean) {
		super.onHiddenChanged(hidden)
		if (!hidden) blogsCallback?.setTitle(getString(R.string.blogs_list))
	}

	override fun getLoadMore() = onLoadMore

	override fun onNotifyAdapter(articles: ArrayList<BlogArticles.Article>, page: Int) {
		hideProgress()
		if (articles.isEmpty()) {
			adapter.clear()
			stateLayout.showEmptyState()
			return
		}
		if (page <= 1) {
			adapter.insertItems(articles)
		} else {
			adapter.addItems(articles)
		}
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
		presenter.onCallApi(1, blogId)
	}

	override fun onItemClicked(item: BlogArticles.Article) {
		BlogArticleOverviewActivity.startActivity(context!!, item)
	}

	override fun onItemLongClicked(position: Int, v: View?, item: BlogArticles.Article) {
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
		val TAG: String = BlogsArticlesFragment::class.java.simpleName

		fun newInstance(blogId: Int, blogTitle: String, isClosed: Boolean): BlogsArticlesFragment {
			val view = BlogsArticlesFragment()
			view.arguments = Bundler.start()
					.put(BundleConstant.EXTRA, blogId)
					.put(BundleConstant.EXTRA_TWO, blogTitle)
					.put(BundleConstant.YES_NO_EXTRA, isClosed)
					.end()
			return view
		}
	}

}