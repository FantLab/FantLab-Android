package ru.fantlab.android.ui.modules.classificator

import io.reactivex.functions.Consumer
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ClassificatorPagerPresenter : BasePresenter<ClassificatorPagerMvp.View>(),
		ClassificatorPagerMvp.Presenter {

	override fun onSendClassification(workId: Int, query: String) {
		makeRestCall(
				DataManager.sendClassification(workId, query)
						.toObservable(),
				Consumer { response ->
					sendToView { it.onClassSended() }
				}
		)
	}

}