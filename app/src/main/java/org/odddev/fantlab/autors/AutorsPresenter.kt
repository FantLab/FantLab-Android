package org.odddev.fantlab.autors

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.odddev.fantlab.core.di.Injector
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

@InjectViewState
class AutorsPresenter : MvpPresenter<IAutorsView>() {

	private var autors: List<Autor>? = null

	@Inject
	lateinit var compositeSubscription: CompositeSubscription

	@Inject
	lateinit var autorsProvider: IAutorsProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun getAutors() {
		if (autors != null) {
			showAutors(autors as List<Autor>)
		} else {
			compositeSubscription.add(autorsProvider
					.getAutors()
					.subscribe(
							{ autors ->
								this.autors = autors.getAutorsList()
								showAutors(this.autors as List<Autor>)
							},
							{ error ->
								viewState.showError(error.message ?: "Error")
							}))
		}
	}

	private fun showAutors(autors: List<Autor>) {
		viewState.showAutors(autors)
	}

	override fun onDestroy() {
		compositeSubscription.unsubscribe()
	}
}
