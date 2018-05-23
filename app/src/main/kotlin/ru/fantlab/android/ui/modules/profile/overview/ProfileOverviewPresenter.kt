package ru.fantlab.android.ui.modules.profile.overview

import android.os.Bundle
import io.reactivex.functions.Consumer
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ProfileOverviewPresenter : BasePresenter<ProfileOverviewMvp.View>(), ProfileOverviewMvp.Presenter {

	@com.evernote.android.state.State
	var userId: Int? = null

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or User is null")
		}
		userId = bundle.getInt(BundleConstant.EXTRA)
		userId?.let {
			makeRestCall(
					DataManager.getUser(it)
							.map { it.get() }
							.toObservable(),
					Consumer { response ->
						sendToView { it.onInitViews(response.user) }
					}
			)
		}
	}

	override fun onError(throwable: Throwable) {
		userId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showErrorMessage("Не удалось загрузить данные") }
	}
}