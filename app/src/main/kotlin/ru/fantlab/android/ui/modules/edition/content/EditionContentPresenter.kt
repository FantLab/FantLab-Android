package ru.fantlab.android.ui.modules.edition.content

import android.os.Bundle
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.EditionContent
import ru.fantlab.android.data.dao.response.EditionResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getEditionPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class EditionContentPresenter : BasePresenter<EditionContentMvp.View>(),
		EditionContentMvp.Presenter {

	private var editionId: Int = -1

	override fun onFragmentCreated(bundle: Bundle) {
		editionId = bundle.getInt(BundleConstant.EXTRA)
		getContent(false)
	}

	override fun getContent(force: Boolean) {
		makeRestCall(
				getContentInternal(force).toObservable(),
				Consumer { content -> sendToView { it.onInitViews(content) } }
		)
	}

	private fun getContentInternal(force: Boolean) =
			getContentFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getContentFromDb()
						} else {
							throw throwable
						}
					}

	private fun getContentFromServer(): Single<ArrayList<EditionContent>> =
			DataManager.getEdition(editionId, true)
					.map { getContent(it) }

	private fun getContentFromDb(): Single<ArrayList<EditionContent>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getEditionPath(editionId, true))
					.map { it.toNullable()!!.response }
					.map { EditionResponse.Deserializer().deserialize(it) }
					.map { getContent(it) }

	private fun getContent(response: EditionResponse): ArrayList<EditionContent> =
			response.editionContent
}