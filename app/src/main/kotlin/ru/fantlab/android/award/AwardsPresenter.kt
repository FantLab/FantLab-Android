package ru.fantlab.android.award

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.fantlab.android.core.di.Injector
import timber.log.Timber
import javax.inject.Inject

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
		awards?.let { viewState.showAwards(it) } ?: run {
			disposables.add(awardsProvider
					.getAwards()
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(
							{ awards ->
								this.awards = awards
								viewState.showAwards(awards)
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
