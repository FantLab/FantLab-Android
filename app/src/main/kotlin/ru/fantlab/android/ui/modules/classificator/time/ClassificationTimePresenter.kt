package ru.fantlab.android.ui.modules.classificator.time

import android.os.Bundle
import io.reactivex.Single
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.helper.single
import ru.fantlab.android.provider.storage.ClassificatoriesProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ClassificationTimePresenter : BasePresenter<ClassificationTimeMvp.View>(),
		ClassificationTimeMvp.Presenter {

	override fun onFragmentCreated(bundle: Bundle) {
		manageDisposable(
				Single.fromCallable {
					val classif = FantlabHelper.ClassificatorTypes.TYPE_TIME.tag
					ClassificatoriesProvider.loadClasses(classif).items
				}.single()
						.subscribe(
								{ classificators -> sendToView { it.onInitViews(classificators) } }
						) { throwable -> onError(throwable) }
		)
	}
}