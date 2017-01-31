package org.odddev.fantlab.award

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
class AwardsPresenter : MvpPresenter<IAwardsView>() {

	private var awards: List<Award>? = null

	@Inject
	lateinit var compositeSubscription: CompositeSubscription

	@Inject
	lateinit var awardsProvider: IAwardsProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun getAwards() {
		if (awards != null) {
			showAwards(awards as List<Award>)
		} else {
			compositeSubscription.add(awardsProvider
					.getAwards()
					.subscribe(
							{ awards ->
								this.awards = awards
								showAwards(awards)
							},
							{ error ->
								viewState.showError(error.message ?: "")
							}))
		}
	}

	private fun showAwards(awards: List<Award>) {
		viewState.showAwards(awards)
	}

	override fun onDestroy() {
		compositeSubscription.unsubscribe()
		super.onDestroy()
	}
}
