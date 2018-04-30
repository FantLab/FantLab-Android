package ru.fantlab.android.ui.modules.edition.overview

import android.os.Bundle
import io.reactivex.functions.Consumer
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import timber.log.Timber

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
					DataManager.getEdition(it, false, false)
							.map { it.get() }
							.toObservable(),
					Consumer { edition ->
						Timber.d(edition.toString())
						sendToView { it.onInitViews(edition) }
					}
			)}
	}

	override fun onError(throwable: Throwable) {
		editionId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		// todo загрузить из базы и распарсить
		sendToView { it.showErrorMessage("Не удалось загрузить данные") }
	}
}