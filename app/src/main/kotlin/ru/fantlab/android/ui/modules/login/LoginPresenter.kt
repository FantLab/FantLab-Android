package ru.fantlab.android.ui.modules.login

import com.github.kittinunf.fuel.core.FuelError
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.Login
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class LoginPresenter : BasePresenter<LoginMvp.View>(), LoginMvp.Presenter {

	override fun proceedWithoutLogin() {
		PrefGetter.setProceedWithoutLogin(true)
		sendToView { it.onSuccessfullyLoggedIn() }
	}

	override fun login(username: String, password: String) {
		val usernameIsEmpty = InputHelper.isEmpty(username)
		val passwordIsEmpty = InputHelper.isEmpty(password)
		sendToView { it.onEmptyUserName(usernameIsEmpty) }
		sendToView { it.onEmptyPassword(passwordIsEmpty) }
		if (!usernameIsEmpty && !passwordIsEmpty) {
			manageDisposable(
					DataManager.login(username, password).toObservable()
							.subscribe({ response -> onLoginResponse(response.login) }) { throwable ->
								if (throwable is FuelError) when (throwable.response.statusCode) {
									401 -> sendToView { it.onWrongPassword() }
									404 -> sendToView { it.onWrongUsername() }
									500 -> sendToView { it.showSignInFailed() }
								} else sendToView { it.showSignInFailed() }
							}
			)
		}
	}

	private fun onLoginResponse(login: Login) {
		PrefGetter.setToken(login.token)
		makeRestCall(
				DataManager.getUser(login.userId.toInt()).toObservable(),
				Consumer { onUserResponse(it.user) }
		)
	}

	private fun onUserResponse(user: User) {
		if (user.blocked == 1) {
			if (user.blockEndDate != null) {
				sendToView { it.showUserBlocked(user.blockEndDate) }
			} else {
				sendToView { it.showUserBlockedForever() }
			}
		} else {
			manageDisposable(
					Single.fromCallable {
						PrefGetter.setLoggedUser(user)
						PrefGetter.setProceedWithoutLogin(false)
						App.instance.initFuel()
					}.doOnSuccess { _ -> sendToView { it.onSuccessfullyLoggedIn() } }
							.subscribeOn(Schedulers.io())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe()
			)
		}
	}
}