package org.odddev.fantlab.autors

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import org.odddev.fantlab.core.di.Injector
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class AutorsPresenter : MvpPresenter<IAutorsView>() {

	private var autors: List<Autor>? = null

	@Inject
	lateinit var disposables: CompositeDisposable

	@Inject
	lateinit var autorsProvider: IAutorsProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun getAutors() {
		autors?.let { viewState.showAutors(it) } ?: run {
			disposables.add(autorsProvider
					.getAutors()
					.subscribe(
							{ autors ->
								this.autors = autors.getAutorsList()
								viewState.showAutors(this.autors as List<Autor>)
							},
							{ error -> run {
								Timber.e(error)
								viewState.showError(error.message ?: "Error")
							}}))
		}
	}

	override fun onDestroy() {
		disposables.clear()
	}
}
