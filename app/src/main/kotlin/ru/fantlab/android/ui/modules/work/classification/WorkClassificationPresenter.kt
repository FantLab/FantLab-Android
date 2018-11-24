package ru.fantlab.android.ui.modules.work.classification

import android.os.Bundle
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.GenreGroup
import ru.fantlab.android.data.dao.response.WorkResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getWorkPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkClassificationPresenter : BasePresenter<WorkClassificationMvp.View>(),
		WorkClassificationMvp.Presenter {

	private var workId = -1

	override fun onFragmentCreated(bundle: Bundle) {
		workId = bundle.getInt(BundleConstant.EXTRA)
		getClassificatory(false)
	}

	override fun getClassificatory(force: Boolean) {
		makeRestCall(
				getClassificatoryInternal(workId).toObservable(),
				Consumer { classificatory -> sendToView { it.onInitViews(classificatory) } }
		)
	}

	private fun getClassificatoryInternal(workId: Int) =
			getClassificatoryFromServer(workId)
					.onErrorResumeNext {
						getClassificatoryFromDb(workId)
					}

	private fun getClassificatoryFromServer(workId: Int): Single<ArrayList<GenreGroup>> =
			DataManager.getWork(workId, showClassificatory = true)
					.map { getClassificatory(it) }

	private fun getClassificatoryFromDb(workId: Int): Single<ArrayList<GenreGroup>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getWorkPath(workId, showClassificatory = true))
					.map { it.response }
					.map { WorkResponse.Deserializer().deserialize(it) }
					.map { getClassificatory(it) }

	private fun getClassificatory(response: WorkResponse): ArrayList<GenreGroup> =
			response.classificatory
}