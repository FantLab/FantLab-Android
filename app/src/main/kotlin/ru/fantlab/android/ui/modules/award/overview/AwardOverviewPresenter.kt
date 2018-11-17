package ru.fantlab.android.ui.modules.award.overview

import android.os.Bundle
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.data.dao.response.AwardResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getAwardPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AwardOverviewPresenter : BasePresenter<AwardOverviewMvp.View>(),
		AwardOverviewMvp.Presenter {

	override fun onFragmentCreated(bundle: Bundle) {
		val awardId = bundle.getInt(BundleConstant.EXTRA)
		makeRestCall(
				getAwardInternal(awardId).toObservable(),
				Consumer { award -> sendToView { it.onInitViews(award) } }
		)
	}

	private fun getAwardInternal(awardId: Int) =
			getAwardFromServer(awardId)
					.onErrorResumeNext {
						getAwardFromDb(awardId)
					}

	private fun getAwardFromServer(awardId: Int): Single<Award> =
			DataManager.getAward(awardId, false, false)
					.map { getAward(it) }

	private fun getAwardFromDb(awardId: Int): Single<Award> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getAwardPath(awardId, false, false))
					.map { it.toNullable()!!.response }
					.map { AwardResponse.Deserializer().deserialize(it) }
					.map { getAward(it) }

	private fun getAward(response: AwardResponse): Award = response.award
}