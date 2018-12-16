package ru.fantlab.android.ui.modules.award.contests

import android.os.Bundle
import android.view.View
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.data.dao.response.AwardResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getAwardPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AwardContestsPresenter : BasePresenter<AwardContestsMvp.View>(),
		AwardContestsMvp.Presenter {

	private var awardId = -1

	override fun onFragmentCreated(bundle: Bundle) {
		awardId = bundle.getInt(BundleConstant.EXTRA)
		getContests(false)
	}

	override fun getContests(force: Boolean) {
		makeRestCall(
				getContestsInternal(force).toObservable(),
				Consumer { contests -> sendToView { it.onInitViews(contests.toNullable()) } }
		)
	}

	private fun getContestsInternal(force: Boolean) =
			getContestsFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getContestsFromDb()
						} else {
							throw throwable
						}
					}

	private fun getContestsFromServer(): Single<Optional<List<Award.Contest>>> =
			DataManager.getAward(awardId, false, true)
					.map { getContests(it) }

	private fun getContestsFromDb(): Single<Optional<List<Award.Contest>>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getAwardPath(awardId, false, true))
					.map { it.response }
					.map { AwardResponse.Deserializer().deserialize(it) }
					.map { getContests(it) }

	private fun getContests(response: AwardResponse): Optional<List<Award.Contest>> =
			response.award.contests.toOptional()

	override fun onItemClick(position: Int, v: View?, item: Award.Contest) {
		sendToView { it.onItemClicked(item, position) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Award.Contest) {
	}
}