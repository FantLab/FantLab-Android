package ru.fantlab.android.ui.modules.plans.pubplans

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Pubplans
import ru.fantlab.android.data.dao.response.PubplansResponse
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.PubplansSortOption
import ru.fantlab.android.provider.rest.getPublisherPubplansPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class PubplansPresenter : BasePresenter<PubplansMvp.View>(),
		PubplansMvp.Presenter {

	private var page: Int = 1
	private var sort: FantlabHelper.PublishersPubplansSort<PubplansSortOption, Int, Int> = FantlabHelper.PublishersPubplansSort(PubplansSortOption.BY_CORRECT, 0, 0)
	private var previousTotal: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE
	var publishers: List<Pubplans.Publisher> = arrayListOf()

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
		getPubplans(page, false)
		return true
	}

	override fun getPubplans(page: Int, force: Boolean) {
		makeRestCall(
				getPubplansInternal(page, force).toObservable(),
				Consumer { (response, totalCount, lastPage) ->
					this.lastPage = lastPage
					sendToView {
						with(it) {
							onNotifyAdapter(response, page)
						}
					}
				}
		)
	}

	private fun getPubplansInternal(page: Int, force: Boolean) =
			getPubplansFromServer(page)
					.onErrorResumeNext { throwable ->
						if (page == 1 && !force) {
							getPubplansFromDb(page)
						} else {
							throw throwable
						}
					}

	private fun getPubplansFromServer(page: Int): Single<Triple<ArrayList<Pubplans.Object>, Int, Int>> =
			DataManager.getPubplans(page, sort.sortBy.value, sort.filterLang, sort.filterPublisher)
					.map { getPubplans(it) }

	private fun getPubplansFromDb(page: Int): Single<Triple<ArrayList<Pubplans.Object>, Int, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getPublisherPubplansPath(page, sort.sortBy.value, sort.filterLang, sort.filterPublisher))
					.map { it.response }
					.map { PubplansResponse.Deserializer().deserialize(it) }
					.map { getPubplans(it) }

	private fun getPubplans(response: PubplansResponse): Triple<ArrayList<Pubplans.Object>, Int, Int> {
		publishers = response.publisherList
		return Triple(response.editions.items, response.editions.totalCount, response.editions.last)
	}

	override fun setCurrentSort(sortBy: PubplansSortOption?, filterLang: String?, filterPublisher: String?) {
		sort.sortBy = sortBy ?: sort.sortBy
		sort.filterLang = filterLang?.toInt() ?: sort.filterLang
		sort.filterPublisher = filterPublisher?.toInt() ?: sort.filterPublisher
		getPubplans(1, true)
	}

	override fun onItemClick(position: Int, v: View?, item: Pubplans.Object) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Pubplans.Object) {
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