package ru.fantlab.android.ui.modules.blogs.main

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Blogs
import ru.fantlab.android.data.dao.response.BlogsResponse
import ru.fantlab.android.provider.rest.BlogsSortOption
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getBlogsPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class BlogsMainPresenter : BasePresenter<BlogsMainMvp.View>(),
		BlogsMainMvp.Presenter {

	private var page: Int = 1
	private var lastPage: Int = Integer.MAX_VALUE
	private var previousTotal: Int = 0
	private var sort = BlogsSortOption.BY_UPDATE

	override fun onCallApi(page: Int, parameter: Int?): Boolean {
		if (page == 1) {
			lastPage = Integer.MAX_VALUE
			sendToView { it.getLoadMore().reset() }
		}
		setCurrentPage(page)
		if (page > lastPage || lastPage == 0) {
			sendToView { it.hideProgress() }
			return false
		}
		getBlogs(false)
		return true
	}

	override fun getBlogs(force: Boolean) {
		makeRestCall(
				getBlogsInternal(force).toObservable(),
				Consumer { (blogs, lastPage) -> sendToView {
					this.lastPage = lastPage
					it.getLoadMore().setTotalPagesCount(lastPage)
					it.onNotifyAdapter(blogs, page)
				} }
		)
	}

	private fun getBlogsInternal(force: Boolean) =
			getForumFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getForumFromDb()
						} else {
							throw throwable
						}
					}

	private fun getForumFromServer(): Single<Pair<ArrayList<Blogs.Blog>, Int>> =
			DataManager.getBlogs(page, 25, sort)
					.map { getBlogs(it) }

	private fun getForumFromDb(): Single<Pair<ArrayList<Blogs.Blog>, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getBlogsPath(page, 25, sort))
					.map { it.response }
					.map { BlogsResponse.Deserializer().deserialize(it) }
					.map { this.getBlogs(it) }

	private fun getBlogs(response: BlogsResponse): Pair<ArrayList<Blogs.Blog>, Int> = response.blogs.items to response.blogs.last

	override fun getCurrentPage(): Int = page

	override fun getPreviousTotal(): Int = previousTotal

	override fun setCurrentPage(page: Int) {
		this.page = page
	}

	override fun getCurrentSort() = sort

	override fun setCurrentSort(sortValue: String) {
		sort = BlogsSortOption.valueOf(sortValue)
		onCallApi(1, null)
	}

	override fun setPreviousTotal(previousTotal: Int) {
		this.previousTotal = previousTotal
	}

	override fun onItemClick(position: Int, v: View?, item: Blogs.Blog) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Blogs.Blog) {
		sendToView { it.onItemLongClicked(position, v, item) }
	}
}