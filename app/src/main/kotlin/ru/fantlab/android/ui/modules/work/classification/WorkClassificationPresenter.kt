package ru.fantlab.android.ui.modules.work.classification

import android.os.Bundle
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.ClassificationGenre
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkClassificationPresenter : BasePresenter<WorkClassificationMvp.View>(),
		WorkClassificationMvp.Presenter {

	@com.evernote.android.state.State var workId: Int? = null
	private var responses: ArrayList<ClassificationGenre> = arrayListOf()

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or Work is null")
		}
		workId = bundle.getInt(BundleConstant.EXTRA)
		workId?.let {
			makeRestCall(
					DataManager.getWork(it, showClassificatory = true)
							.map { it.get() }
							.toObservable(),
					Consumer { workResponse ->
						sendToView { it.onInitViews(workResponse.classificatory) }
					}
			)
		}
	}

	override fun getResponses(): ArrayList<ClassificationGenre> = responses

	override fun onError(throwable: Throwable) {
		workId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showErrorMessage("Не удалось загрузить данные") }
	}
}