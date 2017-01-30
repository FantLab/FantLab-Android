package org.odddev.fantlab.auth.reg

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.odddev.fantlab.auth.IAuthProvider
import org.odddev.fantlab.core.di.Injector
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * @author kenrube
 * *
 * @since 20.09.16
 */

@InjectViewState
class RegPresenter internal constructor() : MvpPresenter<IRegView>() {

	private var regSubscription: Subscription? = null

	@Inject
	lateinit var compositeSubscription: CompositeSubscription

	@Inject
	lateinit var authProvider: IAuthProvider

	init {
		Injector.getAppComponent().inject(this)
	}

	fun register(validator: RegValidator) {
		regSubscription = authProvider
				.register(
						validator.fields[RegValidator.USERNAME] as String,
						validator.fields[RegValidator.PASSWORD] as String,
						validator.fields[RegValidator.EMAIL] as String)
				.subscribe(
						{ this.showResult(it) }
				)
		compositeSubscription.add(regSubscription!!)
	}

	private fun showResult(hasCookie: Boolean) {
		viewState.showRegResult(hasCookie)
	}

	/*private fun showError(error: String) {
		viewState.showError(error)
	}*/

	override fun onDestroy() {
		compositeSubscription.unsubscribe()
		super.onDestroy()
	}
}
