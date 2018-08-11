package ru.fantlab.android.ui.modules.award.nominations

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AwardNominationsPresenter : BasePresenter<AwardNominationsMvp.View>(),
		AwardNominationsMvp.Presenter {

	@com.evernote.android.state.State
	var awardId: Int? = null
	private var nominations: ArrayList<Award.Nominations> = ArrayList()

	override fun onError(throwable: Throwable) {
		awardId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showErrorMessage("Не удалось загрузить данные") }
	}

	override fun getAwardNominations(): ArrayList<Award.Nominations> = nominations

	fun onCallApi(parameter: Int?) {
		awardId = parameter
		awardId?.let { it ->
			makeRestCall(
					DataManager.getAward(it, true, false)
							.map { it.get() }
							.toObservable(),
					Consumer { AwardNominationsResponse ->
						sendToView {
							it.onNotifyAdapter(AwardNominationsResponse.award.nominations)
							it.onSetTabCount(AwardNominationsResponse.award.nominations?.size ?: 0)
						}
					}
			)}
	}

	override fun onItemClick(position: Int, v: View?, item: Award.Nominations) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Award.Nominations?) {
	}
}