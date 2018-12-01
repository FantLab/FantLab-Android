package ru.fantlab.android.ui.modules.work.editions

import android.os.Bundle
import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.data.dao.model.EditionsInfo
import ru.fantlab.android.data.dao.response.WorkResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getWorkPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkEditionsPresenter : BasePresenter<WorkEditionsMvp.View>(),
		WorkEditionsMvp.Presenter {

	private var workId = -1

	override fun onFragmentCreated(bundle: Bundle) {
		workId = bundle.getInt(BundleConstant.EXTRA)
		getEditions(false)
	}

	override fun getEditions(force: Boolean) {
		makeRestCall(
				getEditionsInternal(force).toObservable(),
				Consumer { (editionsBlocks, editionsInfo) ->
					sendToView { it.onInitViews(editionsBlocks, editionsInfo!!) }
				}
		)
	}

	private fun getEditionsInternal(force: Boolean) =
			getEditionsFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getEditionsFromDb()
						} else {
							throw throwable
						}
					}
					.onErrorResumeNext { ext -> Single.error(ext) }
					.doOnError { err -> sendToView { it.hideProgress() } }

	private fun getEditionsFromServer(): Single<Pair<EditionsBlocks?, EditionsInfo?>> =
			DataManager.getWork(workId, showEditionsBlocks = true, showEditionsInfo = true)
					.map { getEditions(it) }

	private fun getEditionsFromDb(): Single<Pair<EditionsBlocks?, EditionsInfo?>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getWorkPath(workId, showEditionsBlocks = true, showEditionsInfo = true))
					.map { it.response }
					.map { WorkResponse.Deserializer().deserialize(it) }
					.map { getEditions(it) }

	private fun getEditions(response: WorkResponse): Pair<EditionsBlocks?, EditionsInfo?> =
			response.editionsBlocks to response.editionsInfo

	override fun onItemClick(position: Int, v: View?, item: EditionsBlocks.Edition) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: EditionsBlocks.Edition) {
	}
}