package org.odddev.fantlab.auth

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.storage.StorageManager
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class AuthPresenter : MvpPresenter<IAuthView>() {

	@Inject
	lateinit var compositeDisposable: CompositeDisposable

	@Inject
	lateinit var authProvider: IAuthProvider

	@Inject
	lateinit var storageManager: StorageManager

	init {
		Injector.getAppComponent().inject(this)
	}

	fun login(validator: AuthValidator) {
		if (validator.areFieldsValid()) {
			compositeDisposable.add(authProvider
					.login(validator.fields[AuthValidator.USERNAME] as String,
							validator.fields[AuthValidator.PASSWORD] as String)
					.subscribe(
							{
								if (it) {
									storageManager.clearAnonymous()
								}
								this.showResult(it)
							},
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

	fun setAnonymous() = storageManager.saveAnonymous()

	private fun showResult(hasCookie: Boolean) = viewState.showAuthResult(hasCookie)

	private fun showError(error: String) = viewState.showError(error)

	private fun showFieldsInvalid() = viewState.showFieldsInvalid()

	override fun onDestroy() = compositeDisposable.clear()
}