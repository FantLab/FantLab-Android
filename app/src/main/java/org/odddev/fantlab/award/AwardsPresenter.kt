package org.odddev.fantlab.award

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

import org.odddev.fantlab.core.di.Injector

import javax.inject.Inject

import rx.subscriptions.CompositeSubscription

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

@InjectViewState
class AwardsPresenter : MvpPresenter<IAwardsView>() {

	private var awards: List<Award>? = null

	@Inject
	internal var compositeSubscription: CompositeSubscription? = null

	@Inject
	internal var awardsProvider: IAwardsProvider? = null

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun getAwards() {
		if (awards != null) {
			showAwards(awards)
		} else {
			compositeSubscription!!.add(awardsProvider!!
					.getAwards()
					.subscribe(
							{ awards ->
								this.awards = awards
								showAwards(awards)
							}
					) { throwable -> showError(throwable.getLocalizedMessage()) })
		}
	}

	private fun showAwards(awards: List<Award>) {
		viewState.showAwards(awards)
	}

	private fun showError(error: String) {
		viewState.showError(error)
	}

	override fun onDestroy() {
		compositeSubscription!!.unsubscribe()
		super.onDestroy()
	}
}
