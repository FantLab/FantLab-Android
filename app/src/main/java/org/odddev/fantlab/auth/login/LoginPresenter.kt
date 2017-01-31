package org.odddev.fantlab.auth.login

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.odddev.fantlab.auth.IAuthProvider
import org.odddev.fantlab.core.di.Injector
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * @author kenrube
 * *
 * @since 30.08.16
 */

@InjectViewState
class LoginPresenter : MvpPresenter<ILoginView>() {

	@Inject
	lateinit var compositeSubscription: CompositeSubscription

	@Inject
	lateinit var authProvider: IAuthProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	fun login(validator: LoginValidator) {
		if (validator.areFieldsValid()) {
			compositeSubscription.add(authProvider
					.login(validator.fields[LoginValidator.USERNAME] as String,
							validator.fields[LoginValidator.PASSWORD] as String)
					.subscribe(
							{ this.showResult(it) }
					)
			)
		} else {
			showFieldsInvalid()
		}
	}

	private fun showResult(hasCookie: Boolean) {
		viewState.showLoginResult(hasCookie)
	}

	/*private fun showError(error: String) {
		viewState.showError(error)
	}*/

	private fun showFieldsInvalid() {
		viewState.showFieldsInvalid()
	}

	override fun onDestroy() {
		compositeSubscription.unsubscribe()
		super.onDestroy()
	}
}
