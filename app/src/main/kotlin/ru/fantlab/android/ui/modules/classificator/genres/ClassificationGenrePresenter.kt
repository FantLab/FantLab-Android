package ru.fantlab.android.ui.modules.classificator.genres

import android.os.Bundle
import io.reactivex.Single
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.helper.single
import ru.fantlab.android.provider.storage.ClassificatoriesProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ClassificationGenrePresenter : BasePresenter<ClassificationGenreMvp.View>(),
		ClassificationGenreMvp.Presenter {

	override fun onFragmentCreated(bundle: Bundle) {
		manageDisposable(
				Single.fromCallable {
					val classif = FantlabHelper.ClassificatorTypes.TYPE_GENRE.tag
					ClassificatoriesProvider.loadClasses(classif).items
				}.single()
						.subscribe(
								{ classificators -> sendToView { it.onInitViews(classificators) } }
						) { throwable -> onError(throwable) }
		)
	}
}