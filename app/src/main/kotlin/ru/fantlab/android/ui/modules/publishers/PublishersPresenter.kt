package ru.fantlab.android.ui.modules.publishers

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Publishers
import ru.fantlab.android.data.dao.response.PublishersResponse
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.PublishersSortOption
import ru.fantlab.android.provider.rest.getPublishersAllPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class PublishersPresenter : BasePresenter<PublishersMvp.View>(),
		PublishersMvp.Presenter {

	private var page: Int = 1
	private var sort: FantlabHelper.PublishersAllSort<PublishersSortOption, Int, Int> = FantlabHelper.PublishersAllSort(PublishersSortOption.BY_NAME, 0, 0)
	private var previousTotal: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE

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
		getPublishers(page, false)
		return true
	}

	override fun getPublishers(page: Int, force: Boolean) {
		makeRestCall(
				getPublishersInternal(page, force).toObservable(),
				Consumer { (publishers, totalCount, lastPage) ->
					this.lastPage = lastPage
					sendToView {
						with (it) {
							onNotifyAdapter(publishers, page)
						}
					}
				}
		)
	}

	private fun getPublishersInternal(page: Int, force: Boolean) =
			getPublishersFromServer(page)
					.onErrorResumeNext { throwable ->
						if (page == 1 && !force) {
							getPublishersFromDb(page)
						} else {
							throw throwable
						}
					}

	private fun getPublishersFromServer(page: Int): Single<Triple<ArrayList<Publishers.Publisher>, Int, Int>> =
			DataManager.getPublishers(page, sort.sortBy.value, sort.filterCountry, sort.filterCategory)
					.map { getPublishers(it) }

	private fun getPublishersFromDb(page: Int): Single<Triple<ArrayList<Publishers.Publisher>, Int, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getPublishersAllPath(page, sort.sortBy.value, sort.filterCountry, sort.filterCategory))
					.map { it.response }
					.map { PublishersResponse.Deserializer(perPage = 250).deserialize(it) }
					.map { getPublishers(it) }

	private fun getPublishers(response: PublishersResponse): Triple<ArrayList<Publishers.Publisher>, Int, Int> =
			Triple(response.publishers.items, response.publishers.totalCount, response.publishers.last)

	override fun setCurrentSort(sortBy: PublishersSortOption?, filterCountry: String?, filterCategory: String?) {
		sort.sortBy = sortBy ?: sort.sortBy
		sort.filterCountry = filterCountry?.toInt() ?: sort.filterCountry
		sort.filterCategory = filterCategory?.toInt() ?: sort.filterCategory
		getPublishers(1, true)
	}

	override fun onItemClick(position: Int, v: View?, item: Publishers.Publisher) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Publishers.Publisher) {
		sendToView { it.onItemLongClicked(position, v, item) }
	}

	override fun getCurrentPage(): Int = page

	override fun getPreviousTotal(): Int = previousTotal

	override fun setCurrentPage(page: Int) {
		this.page = page
	}

	override fun setPreviousTotal(previousTotal: Int) {
		this.previousTotal = previousTotal
	}
}