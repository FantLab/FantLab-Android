package ru.fantlab.android.ui.modules.classificator.story

import android.os.Bundle
import io.reactivex.Single
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.helper.single
import ru.fantlab.android.provider.storage.ClassificatoriesProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ClassificationStoryPresenter : BasePresenter<ClassificationStoryMvp.View>(),
		ClassificationStoryMvp.Presenter {

	override fun onFragmentCreated(bundle: Bundle) {
		manageDisposable(
				Single.fromCallable {
					val classif = FantlabHelper.ClassificatorTypes.TYPE_STORY.tag
					ClassificatoriesProvider.loadClasses(classif).items
				}.single()
						.subscribe(
								{ classificators -> sendToView { it.onInitViews(classificators) } }
						) { throwable -> onError(throwable) }
		)
	}
}