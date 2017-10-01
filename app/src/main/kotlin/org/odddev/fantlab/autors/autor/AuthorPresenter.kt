package org.odddev.fantlab.autors.autor

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import org.odddev.fantlab.core.di.Injector
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class AuthorPresenter : MvpPresenter<IAutorView>() {

	private var autor: AutorFull? = null

	@Inject
	lateinit var disposables: CompositeDisposable

	@Inject
	lateinit var authorProvider: IAuthorProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun getAutor(id: Int) {
		autor?.let { viewState.showAutor(it) } ?: run {
			disposables.add(authorProvider
					.getAuthor(id)
					.subscribe(
							{
								authors -> viewState.showError("Ready")
							},
							{ error ->
								run {
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
