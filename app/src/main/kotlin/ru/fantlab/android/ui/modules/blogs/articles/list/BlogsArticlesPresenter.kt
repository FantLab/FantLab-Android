package ru.fantlab.android.ui.modules.blogs.articles.list

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.BlogArticles
import ru.fantlab.android.data.dao.response.BlogArticlesResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getBlogArticlesPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class BlogsArticlesPresenter : BasePresenter<BlogsArticlesMvp.View>(),
		BlogsArticlesMvp.Presenter {

	private var blogId: Int = -1
	private var page: Int = 1
	private var lastPage: Int = Integer.MAX_VALUE
	private var previousTotal: Int = 0

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
		blogId = parameter ?: -1
		getArticles(false)
		return true
	}

	override fun getArticles(force: Boolean) {
		makeRestCall(
				getBlogsInternal(force).toObservable(),
				Consumer { (articles, lastPage) -> sendToView {
					this.lastPage = lastPage
					it.getLoadMore().setTotalPagesCount(lastPage)
					it.onNotifyAdapter(articles, page)
				} }
		)
	}

	private fun getBlogsInternal(force: Boolean) =
			getBlogsFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getBlogsFromDb()
						} else {
							throw throwable
						}
					}

	private fun getBlogsFromServer(): Single<Pair<ArrayList<BlogArticles.Article>, Int>> =
			DataManager.getBlogArticles(blogId, page, 25)
					.map { getArticles(it) }

	private fun getBlogsFromDb(): Single<Pair<ArrayList<BlogArticles.Article>, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getBlogArticlesPath(blogId, page, 25))
					.map { it.response }
					.map { BlogArticlesResponse.Deserializer().deserialize(it) }
					.map { this.getArticles(it) }

	private fun getArticles(response: BlogArticlesResponse): Pair<ArrayList<BlogArticles.Article>, Int> = response.articles.items to response.articles.last

	override fun getCurrentPage(): Int = page

	override fun getPreviousTotal(): Int = previousTotal

	override fun setCurrentPage(page: Int) {
		this.page = page
	}

	override fun setPreviousTotal(previousTotal: Int) {
		this.previousTotal = previousTotal
	}

	override fun onItemClick(position: Int, v: View?, item: BlogArticles.Article) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: BlogArticles.Article) {
		sendToView { it.onItemLongClicked(position, v, item) }
	}
}