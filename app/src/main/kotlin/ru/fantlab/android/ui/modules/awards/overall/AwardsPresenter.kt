package ru.fantlab.android.ui.modules.awards.overall

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.AwardInList
import ru.fantlab.android.data.dao.response.AwardsResponse
import ru.fantlab.android.provider.rest.AwardsSortOption
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getAwardsPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AwardsPresenter : BasePresenter<AwardsMvp.View>(), AwardsMvp.Presenter {

	var sort = AwardsSortOption.BY_NAME
		set(value) {
			field = value
			getAwards(true)
		}

	override fun getAwards(force: Boolean) {
		makeRestCall(
				getAwardsInternal(force).toObservable(),
				Consumer { awards -> sendToView { it.onNotifyAdapter(awards) } }
		)
	}

	private fun getAwardsInternal(force: Boolean) =
			getAwardsFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getAwardsFromDb()
						} else {
							throw throwable
						}
					}

	private fun getAwardsFromServer(): Single<ArrayList<AwardInList>> =
			DataManager.getAwards(true, sort)
					.map { getAwards(it) }

	private fun getAwardsFromDb(): Single<ArrayList<AwardInList>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getAwardsPath(true, sort))
					.map { it.response }
					.map { AwardsResponse.Deserializer().deserialize(it) }
					.map { getAwards(it) }

	private fun getAwards(response: AwardsResponse): ArrayList<AwardInList> = response.awards

	override fun onItemClick(position: Int, v: View?, item: AwardInList) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: AwardInList) {
	}
}