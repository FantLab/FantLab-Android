package ru.fantlab.android.ui.modules.plans.autplans

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Autplans
import ru.fantlab.android.data.dao.response.AutplansResponse
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.AutplansSortOption
import ru.fantlab.android.provider.rest.getPublisherAutplansPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AutPlansPresenter : BasePresenter<AutPlansMvp.View>(),
		AutPlansMvp.Presenter {

	private var page: Int = 1
	private var sort: FantlabHelper.PublishersAutPlansSort<AutplansSortOption, Int, Int> = FantlabHelper.PublishersAutPlansSort(AutplansSortOption.BY_CORRECT, 0)
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
		getAutPlans(page, false)
		return true
	}

	override fun getAutPlans(page: Int, force: Boolean) {
		makeRestCall(
				getAutPlansInternal(page, force).toObservable(),
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

	private fun getAutPlansInternal(page: Int, force: Boolean) =
			getAutPlansFromServer(page)
					.onErrorResumeNext { throwable ->
						if (page == 1 && !force) {
							getAutPlansFromDb(page)
						} else {
							throw throwable
						}
					}

	private fun getAutPlansFromServer(page: Int): Single<Triple<ArrayList<Autplans.Object>, Int, Int>> =
			DataManager.getAutplans(page, sort.sortBy.value, sort.filterLang)
					.map { getAutPlans(it) }

	private fun getAutPlansFromDb(page: Int): Single<Triple<ArrayList<Autplans.Object>, Int, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getPublisherAutplansPath(page, sort.sortBy.value, sort.filterLang))
					.map { it.response }
					.map { AutplansResponse.Deserializer().deserialize(it) }
					.map { getAutPlans(it) }

	private fun getAutPlans(response: AutplansResponse): Triple<ArrayList<Autplans.Object>, Int, Int> = Triple(response.plans.items, response.plans.totalCount, response.plans.last)


	override fun setCurrentSort(sortBy: AutplansSortOption?, filterLang: String?) {
		sort.sortBy = sortBy ?: sort.sortBy
		sort.filterLang = filterLang?.toInt() ?: sort.filterLang
		getAutPlans(1, true)
	}

	override fun onItemClick(position: Int, v: View?, item: Autplans.Object) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Autplans.Object) {
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