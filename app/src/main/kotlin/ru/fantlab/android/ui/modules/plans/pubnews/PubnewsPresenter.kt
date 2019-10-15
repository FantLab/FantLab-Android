package ru.fantlab.android.ui.modules.plans.pubnews

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Pubnews
import ru.fantlab.android.data.dao.response.PubnewsResponse
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.PubnewsSortOption
import ru.fantlab.android.provider.rest.getPubnewsPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class PubnewsPresenter : BasePresenter<PubnewsMvp.View>(),
		PubnewsMvp.Presenter {

	private var page: Int = 1
	private var sort: FantlabHelper.PubnewsSort<PubnewsSortOption, Int, Int> = FantlabHelper.PubnewsSort(PubnewsSortOption.BY_DATE, 0, 0)
	private var previousTotal: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE
	var publishers: List<Pubnews.Publisher> = arrayListOf()

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
		getPubnews(page, false)
		return true
	}

	override fun getPubnews(page: Int, force: Boolean) {
		makeRestCall(
				getPubnewsInternal(page, force).toObservable(),
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

	private fun getPubnewsInternal(page: Int, force: Boolean) =
			getPubnewsFromServer(page)
					.onErrorResumeNext { throwable ->
						if (page == 1 && !force) {
							getPubnewsFromDb(page)
						} else {
							throw throwable
						}
					}

	private fun getPubnewsFromServer(page: Int): Single<Triple<ArrayList<Pubnews.Object>, Int, Int>> =
			DataManager.getPubnews(page, sort.sortBy.value, sort.filterLang, sort.filterPublisher)
					.map { getPubnews(it) }

	private fun getPubnewsFromDb(page: Int): Single<Triple<ArrayList<Pubnews.Object>, Int, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getPubnewsPath(page, sort.sortBy.value, sort.filterLang, sort.filterPublisher))
					.map { it.response }
					.map { PubnewsResponse.Deserializer().deserialize(it) }
					.map { getPubnews(it) }

	private fun getPubnews(response: PubnewsResponse): Triple<ArrayList<Pubnews.Object>, Int, Int> {
		publishers = response.publisherList
		return Triple(response.editions.items, response.editions.totalCount, response.editions.last)
	}

	override fun setCurrentSort(sortBy: PubnewsSortOption?, filterLang: String?, filterPublisher: String?) {
		sort.sortBy = sortBy ?: sort.sortBy
		sort.filterLang = filterLang?.toInt() ?: sort.filterLang
		sort.filterPublisher = filterPublisher?.toInt() ?: sort.filterPublisher
		getPubnews(1, true)
	}

	override fun onItemClick(position: Int, v: View?, item: Pubnews.Object) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Pubnews.Object) {
		sendToView { it.onItemLongClicked(position, v, item) }
	}

	override fun getCurrentPage(): Int = page

	override fun getPreviousTotal(): Int = previousTotal

	override fun setCurrentPage(page: Int) {
		this.page = page
	}

	override fun getCurrentSort() = sort

	override fun setPreviousTotal(previousTotal: Int) {
		this.previousTotal = previousTotal
	}
}