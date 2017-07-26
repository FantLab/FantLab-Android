package org.odddev.fantlab.autors

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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
							}}
					)
			)
		}
	}

	internal fun filter(query: String) {
		disposables.add(Observable.fromArray(autors)
				.subscribeOn(Schedulers.io())
				.flatMapIterable { author -> author }
				.filter({ author -> author.name.contains(query, true) || author.nameOrig.contains(query, true) })
				.collectInto(ArrayList<Autor>(), { list, author -> list.add(author) })
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						{
							list -> viewState.showAutors(list)
						}
				)
		)
	}

	override fun onDestroy() {
		disposables.clear()
	}
}
