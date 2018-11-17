package ru.fantlab.android.ui.modules.awards

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.AwardInList
import ru.fantlab.android.provider.rest.AwardsSortOption
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AwardsPresenter : BasePresenter<AwardsMvp.View>(), AwardsMvp.Presenter {

	private var awards: ArrayList<AwardInList> = ArrayList()
	private var sort: AwardsSortOption? = null

	override fun getAwards(): ArrayList<AwardInList> = awards

	override fun onItemClick(position: Int, v: View?, item: AwardInList) {
		view?.onItemClicked(item)
	}

	override fun onItemLongClick(position: Int, v: View?, item: AwardInList?) {
	}

	fun setCurrentSort(sortValue: String) {
		sort = AwardsSortOption.valueOf(sortValue)
		onReload()
	}

	override fun onError(throwable: Throwable) {
		onWorkOffline()
		super.onError(throwable)
	}

	override fun onWorkOffline() {
		sendToView { it.showMessage(R.string.error, R.string.failed_data) }
	}

	override fun onReload() {
		makeRestCall(
				DataManager.getAwards(true, sort ?: AwardsSortOption.BY_NAME)
						.map { it.get() }
						.toObservable(),
				Consumer { awardsResponse ->
					sendToView { it.onNotifyAdapter(awardsResponse.awards) }
				}
		)
	}
}