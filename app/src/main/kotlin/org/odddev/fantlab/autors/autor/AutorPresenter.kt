package org.odddev.fantlab.autors.autor

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import org.odddev.fantlab.core.di.Injector
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class AutorPresenter : MvpPresenter<IAutorView>() {

	private var autor: AutorFull? = null

	@Inject
	lateinit var disposables: CompositeDisposable

	@Inject
	lateinit var autorProvider: IAutorProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun getAutor(id: Int) {
		autor?.let { viewState.showAutor(it) } ?: run {
			disposables.add(autorProvider
					.getAutor(id)
					.subscribe(
							{ autor ->
								this.autor = autor
								viewState.showAutor(this.autor as AutorFull)
							},
							{ error -> run {
									Timber.e(error)
									viewState.showError(error.message ?: "Error")
								}
							}))
		}
	}

	override fun onDestroy() {
		disposables.clear()
	}
}
