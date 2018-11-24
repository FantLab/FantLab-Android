package ru.fantlab.android.ui.modules.edition.overview

import android.os.Bundle
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.AdditionalImages
import ru.fantlab.android.data.dao.model.Edition
import ru.fantlab.android.data.dao.response.EditionResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getEditionPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class EditionOverviewPresenter : BasePresenter<EditionOverviewMvp.View>(),
		EditionOverviewMvp.Presenter {

	override fun onFragmentCreated(bundle: Bundle) {
		val editionId = bundle.getInt(BundleConstant.EXTRA)
		makeRestCall(
				getEditionInternal(editionId).toObservable(),
				Consumer { (edition, additionalImages) ->
					sendToView { it.onInitViews(edition, additionalImages) }
				}
		)
	}

	private fun getEditionInternal(editionId: Int) =
			getEditionFromServer(editionId)
					.onErrorResumeNext {
						getEditionFromDb(editionId)
					}

	private fun getEditionFromServer(editionId: Int): Single<Pair<Edition, AdditionalImages?>> =
			DataManager.getEdition(editionId, showAdditionalImages = true)
					.map { getEdition(it) }

	private fun getEditionFromDb(editionId: Int): Single<Pair<Edition, AdditionalImages?>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getEditionPath(editionId, showAdditionalImages = true))
					.map { it.response }
					.map { EditionResponse.Deserializer().deserialize(it) }
					.map { getEdition(it) }

	private fun getEdition(response: EditionResponse): Pair<Edition, AdditionalImages?> =
			response.edition to response.additionalImages
}