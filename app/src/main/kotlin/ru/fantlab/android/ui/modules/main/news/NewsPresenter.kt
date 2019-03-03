package ru.fantlab.android.ui.modules.main.news

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.data.dao.response.NewsResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getNewsPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class NewsPresenter : BasePresenter<NewsMvp.View>(), NewsMvp.Presenter {

	private var page: Int = 1
	private var previousTotal: Int = 0
	private var lastPage = Int.MAX_VALUE

	override fun onFragmentCreated() {
		onCallApi(1)
	}

	override fun onCallApi(page: Int, parameter: String?): Boolean = onCallApi(page)

	override fun onCallApi(page: Int): Boolean {
		if (page == 1) {
			lastPage = Int.MAX_VALUE
			sendToView { it.getLoadMore().reset() }
		}
		if (page > lastPage || lastPage == 0) {
			sendToView { it.hideProgress() }
			return false
		}
		setCurrentPage(page)
		makeRestCall(
				getNewsInternal().toObservable(),
				Consumer { (responses, lastPage) ->
					this.lastPage = lastPage
					sendToView { it.onNotifyAdapter(responses, page) }
				}
		)
		return true
	}

	private fun getNewsInternal() =
			getNewsFromServer()
					.onErrorResumeNext { throwable ->
						if (page == 1) {
							getNewsFromDb()
						} else {
							throw throwable
						}
					}

	private fun getNewsFromServer(): Single<Pair<ArrayList<News>, Int>> =
			DataManager.getNews(page, perPage = 15)
					.map { getNews(it) }

	private fun getNewsFromDb(): Single<Pair<ArrayList<News>, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getNewsPath(page, perPage = 15))
					.map { it.response }
					.map { NewsResponse.Deserializer(perPage = 15).deserialize(it) }
					.map { getNews(it) }

	private fun getNews(response: NewsResponse): Pair<ArrayList<News>, Int> =
			response.news.items to response.news.last

	override fun getCurrentPage(): Int = page

	override fun getPreviousTotal(): Int = previousTotal

	override fun setCurrentPage(page: Int) {
		this.page = page
	}

	override fun setPreviousTotal(previousTotal: Int) {
		this.previousTotal = previousTotal
	}

	override fun onItemClick(position: Int, v: View?, item: News) {
		sendToView { it.onItemClicked(item, v) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: News) {
		sendToView { it.onItemLongClicked(position, v, item) }
	}
}