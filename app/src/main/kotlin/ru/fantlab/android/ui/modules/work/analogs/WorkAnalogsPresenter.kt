package ru.fantlab.android.ui.modules.work.analogs

import android.os.Bundle
import io.reactivex.functions.Consumer
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkAnalogsPresenter : BasePresenter<WorkAnalogsMvp.View>(),
		WorkAnalogsMvp.Presenter {

	@com.evernote.android.state.State
	var workId: Int? = null

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or Work is null")
		}
		workId = bundle.getInt(BundleConstant.EXTRA)
		workId?.let {
			makeRestCall(
					DataManager.getWorkAnalogs(it)
							.map { it.get() }
							.toObservable(),
					Consumer { workAnalogsResponse ->
						sendToView { it.onInitViews(workAnalogsResponse.analogs) }
					}
			)}
	}

	override fun onError(throwable: Throwable) {
		workId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showErrorMessage("Не удалось загрузить данные") }
	}
}