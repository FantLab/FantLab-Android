package ru.fantlab.android.ui.modules.award.nominations

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

class AwardNominationsPresenter : BasePresenter<AwardNominationsMvp.View>(),
		AwardNominationsMvp.Presenter {

	private var awardId = -1

	override fun onFragmentCreated(bundle: Bundle) {
		awardId = bundle.getInt(BundleConstant.EXTRA)
		getNominations(false)
	}

	override fun getNominations(force: Boolean) {
		makeRestCall(
				getNominationsInternal(force).toObservable(),
				Consumer { nominations ->
					sendToView {
						it.onNotifyAdapter(nominations.toNullable())
						it.onSetTabCount(nominations.toNullable()?.size ?: 0)
					}
				}
		)
	}

	private fun getNominationsInternal(force: Boolean) =
			getNominationsFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getNominationsFromDb()
						} else {
							throw throwable
						}
					}

	private fun getNominationsFromServer(): Single<Optional<List<Award.Nominations>>> =
			DataManager.getAward(awardId, true, false)
					.map { getNominations(it) }

	private fun getNominationsFromDb(): Single<Optional<List<Award.Nominations>>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getAwardPath(awardId, true, false))
					.map { it.toNullable()!!.response }
					.map { AwardResponse.Deserializer().deserialize(it) }
					.map { getNominations(it) }

	private fun getNominations(response: AwardResponse): Optional<List<Award.Nominations>> =
			response.award.nominations.toOptional()

	override fun onItemClick(position: Int, v: View?, item: Award.Nominations) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Award.Nominations?) {
	}
}