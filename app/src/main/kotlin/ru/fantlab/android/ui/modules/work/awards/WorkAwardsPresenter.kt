package ru.fantlab.android.ui.modules.work.awards

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Awards
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.data.dao.response.WorkResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getWorkPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkAwardsPresenter : BasePresenter<WorkAwardsMvp.View>(),
		WorkAwardsMvp.Presenter {

	private var workId = -1

	override fun onCallApi(workId: Int) {
		this.workId = workId
		makeRestCall(
				getAwardsInternal(workId).toObservable(),
				Consumer { nominations ->
					sendToView { it.onInitViews(nominations) }
				}
		)
	}

	private fun getAwardsInternal(workId: Int) =
			getAwardsFromServer(workId)
					.onErrorResumeNext {
						getAwardsFromDb(workId)
					}
					.onErrorResumeNext { ext -> Single.error(ext) }
					.doOnError { err -> sendToView { it.showErrorMessage(err.message) } }

	private fun getAwardsFromServer(workId: Int):
			Single<Awards> =
			DataManager.getWork(workId, showAwards = true)
					.map { getAwards(it) }

	private fun getAwardsFromDb(workId: Int):
			Single<Awards> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getWorkPath(workId, showAwards = true))
					.map { it.response }
					.map { WorkResponse.Deserializer().deserialize(it) }
					.map { getAwards(it) }

	private fun getAwards(response: WorkResponse): Awards? = response.awards
}