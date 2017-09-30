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
								author -> Timber.d(
									"authors: ${author.authors.size}\n" +
											"childWorks: ${author.childWorks.size}\n" +
											"laResume: ${author.laResume.size}\n" +
											"nominations: ${author.nominations.size}\n" +
											"pseudonyms: ${author.pseudonyms.size}\n" +
											"sites: ${author.sites.size}\n" +
											"works: ${author.works.size}\n" +
											"workAuthors: ${author.workAuthors.size}\n")
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
