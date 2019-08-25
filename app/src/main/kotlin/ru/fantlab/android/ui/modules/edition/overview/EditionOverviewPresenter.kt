package ru.fantlab.android.ui.modules.edition.overview

import android.os.Bundle
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.AdditionalImages
import ru.fantlab.android.data.dao.model.Edition
import ru.fantlab.android.data.dao.model.EditionContent
import ru.fantlab.android.data.dao.response.EditionResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getEditionPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class EditionOverviewPresenter : BasePresenter<EditionOverviewMvp.View>(),
		EditionOverviewMvp.Presenter {

	private var editionId: Int = -1

	override fun onFragmentCreated(bundle: Bundle) {
		editionId = bundle.getInt(BundleConstant.EXTRA)
		makeRestCall(
				getEditionInternal(editionId).toObservable(),
				Consumer { (edition, additionalImages) ->
					sendToView { it.onInitViews(edition, additionalImages) }
				}
		)
		getContent()
	}

	private fun getEditionInternal(editionId: Int) =
			getEditionFromServer(editionId)
					.onErrorResumeNext {
						getEditionFromDb(editionId)
					}
					.onErrorResumeNext { ext -> Single.error(ext) }
					.doOnError { err -> sendToView { it.onShowErrorView(err.message) } }

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

	fun getContent() {
		makeRestCall(
				getContentInternal().toObservable(),
				Consumer { content -> sendToView { it.onSetContent(content) } }
		)
	}

	private fun getContentInternal() =
			getContentFromServer()
					.onErrorResumeNext {
						getContentFromDb()
					}

	private fun getContentFromServer(): Single<ArrayList<EditionContent>> =
			DataManager.getEdition(editionId, true)
					.map { getContent(it) }

	private fun getContentFromDb(): Single<ArrayList<EditionContent>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getEditionPath(editionId, true))
					.map { it.response }
					.map { EditionResponse.Deserializer().deserialize(it) }
					.map { getContent(it) }

	private fun getContent(response: EditionResponse): ArrayList<EditionContent> =
			response.editionContent
}