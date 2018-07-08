package ru.fantlab.android.ui.modules.edition.overview

import android.os.Bundle
import io.reactivex.functions.Consumer
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class EditionOverviewPresenter : BasePresenter<EditionOverviewMvp.View>(),
		EditionOverviewMvp.Presenter {

	@com.evernote.android.state.State
	var editionId: Int? = null

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or Edition is null")
		}
		editionId = bundle.getInt(BundleConstant.EXTRA)
		editionId?.let {
			makeRestCall(
					DataManager.getEdition(it, showAdditionalImages = true)
							.map { it.get() }
							.toObservable(),
					Consumer { editionResponse ->
						sendToView { it.onInitViews(
								editionResponse.edition,
								editionResponse.additionalImages
						) }
					}
			)}
	}

	override fun onError(throwable: Throwable) {
		editionId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showErrorMessage("Не удалось загрузить данные") }
	}
}