package org.odddev.fantlab.search

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import org.odddev.fantlab.core.di.Injector
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class SearchPresenter : MvpPresenter<ISearchView>() {

	private var searchResult: SearchResult? = null

	@Inject
	lateinit var disposables: CompositeDisposable

	@Inject
	lateinit var searchProvider: ISearchProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun search(query: String) {
		searchResult?.let { viewState.showResults(it) } ?: run {
			disposables.add(searchProvider
					.search(query)
					.subscribe(
							{ result ->
								this.searchResult = result
								viewState.showResults(this.searchResult as SearchResult)
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

	override fun onDestroy() {
		disposables.clear()
	}
}
