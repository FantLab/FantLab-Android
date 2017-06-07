package org.odddev.fantlab.award

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.odddev.fantlab.core.di.Injector
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
	lateinit var disposables: CompositeDisposable

	@Inject
	lateinit var awardsProvider: IAwardsProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	internal fun getAwards() {
		if (awards != null) {
			showAwards(awards as List<Award>)
		} else {
			disposables.add(awardsProvider
					.getAwards()
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(
							{ awards ->
								this.awards = awards
								showAwards(awards)
							},
							{ error ->
								viewState.showError(error.message ?: "Error")
							}))
		}
	}

	private fun showAwards(awards: List<Award>) {
		viewState.showAwards(awards)
	}

	override fun onDestroy() {
		disposables.clear()
	}
}
