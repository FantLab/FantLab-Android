package ru.fantlab.android.ui.modules.award.contests

import android.os.Bundle
import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AwardContestsPresenter : BasePresenter<AwardContestsMvp.View>(),
		AwardContestsMvp.Presenter {

	@com.evernote.android.state.State var awardId: Int? = null
	private var contests: ArrayList<Award.Contest> = ArrayList()

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or awardId is null")
		}
		awardId = bundle.getInt(BundleConstant.EXTRA)
		awardId?.let { it ->
			makeRestCall(
					DataManager.getAward(it, false, true)
							.map { it.get() }
							.toObservable(),
					Consumer { AwardContestsResponse ->
						sendToView {
							it.onInitViews(AwardContestsResponse.award.contests)
						}
					}
			)
		}
	}

	override fun onError(throwable: Throwable) {
		awardId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showErrorMessage("Не удалось загрузить данные") }
	}

	override fun getAwardContests(): ArrayList<Award.Contest> = contests

	fun onCallApi() {
		awardId?.let { it ->
			makeRestCall(
					DataManager.getAward(it, false, true)
							.map { it.get() }
							.toObservable(),
					Consumer { workResponse ->
						sendToView { it.onNotifyAdapter() }
					}
			)
		}
	}

	override fun onItemClick(position: Int, v: View?, item: Award.Contest) {
		sendToView { it.onItemClicked(item, position) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Award.Contest?) {
	}
}