package org.odddev.fantlab.edition

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import org.odddev.fantlab.core.di.Injector
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class EditionPresenter : MvpPresenter<IEditionView>() {

	@Inject
	lateinit var disposables: CompositeDisposable

	@Inject
	lateinit var editionProvider: IEditionProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun getEdition(id: Int) {
		disposables.add(editionProvider
				.getEdition(id)
				.subscribe(
						{
							edition -> viewState.showEdition(edition)
						},
						{ error ->
							run {
								Timber.e(error)
								viewState.showError(error.message ?: "Error")
							}
						}))
	}

	override fun onDestroy() {
		disposables.clear()
	}
}
