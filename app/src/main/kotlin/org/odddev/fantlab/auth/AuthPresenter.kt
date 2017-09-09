package org.odddev.fantlab.auth

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import org.odddev.fantlab.core.di.Injector
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class AuthPresenter : MvpPresenter<IAuthView>() {

	@Inject
	lateinit var compositeDisposable: CompositeDisposable

	@Inject
	lateinit var authProvider: IAuthProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	fun login(validator: AuthValidator) {
		if (validator.areFieldsValid()) {
			compositeDisposable.add(authProvider
					.login(validator.fields[AuthValidator.USERNAME] as String,
							validator.fields[AuthValidator.PASSWORD] as String)
					.subscribe(
							{ this.showResult(it) },
							{ throwable ->
								run {
									this.showError(throwable.localizedMessage)
									Timber.e(throwable)
								}
							}
					)
			)
		} else {
			showFieldsInvalid()
		}
	}

	private fun showResult(hasCookie: Boolean) {
		viewState.showAuthResult(hasCookie)
	}

	private fun showError(error: String) {
		viewState.showError(error)
	}

	private fun showFieldsInvalid() {
		viewState.showFieldsInvalid()
	}

	override fun onDestroy() {
		compositeDisposable.clear()
		super.onDestroy()
	}
}