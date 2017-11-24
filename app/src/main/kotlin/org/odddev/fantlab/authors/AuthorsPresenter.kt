package org.odddev.fantlab.authors

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.odddev.fantlab.author.models.Author
import org.odddev.fantlab.core.di.Injector
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class AuthorsPresenter : MvpPresenter<IAuthorsView>() {

	private var authors: List<Author>? = null

	@Inject
	lateinit var disposables: CompositeDisposable

	@Inject
	lateinit var authorsProvider: IAuthorsProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun getAuthors() {
		authors?.let { viewState.showAuthors(it, false) } ?: run {
			disposables.add(authorsProvider
					.getAuthors()
					.subscribe(
							{ authors ->
								this.authors = authors
								viewState.showAuthors(this.authors as List<Author>, false)
							},
							{ error ->
								run {
									Timber.e(error)
									viewState.showError(error.message ?: "Error")
								}
							}
					)
			)
		}
	}

	internal fun filter(query: String) {
		disposables.add(Observable.fromArray(authors)
				.flatMapIterable { author -> author }
				.filter({ author -> author.rusName?.contains(query, true)!!
						|| author.name?.contains(query, true)!! })
				.collectInto(ArrayList<Author>(), { list, author -> list.add(author) })
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({ list -> viewState.showAuthors(list, true) })
		)
	}

	override fun onDestroy() {
		disposables.clear()
	}
}
