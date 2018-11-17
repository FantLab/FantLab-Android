package ru.fantlab.android.ui.modules.login

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
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
			makeRestCall(
					DataManager.login(username, password).toObservable(),
					Consumer { response ->
						if (response.headers["Location"]?.get(0) == "/loginincorrect") {
							sendToView { it.showSignInFailed() }
							return@Consumer
						}
						onTokenResponse(username, response)
					}
			)
		}
	}

	private fun onTokenResponse(username: String, response: com.github.kittinunf.fuel.core.Response) {
		response.headers["Set-Cookie"]?.map { cookie ->
			if (!InputHelper.isEmpty(cookie) && cookie.startsWith("fl_s")) {
				PrefGetter.setToken(cookie.substring(0, cookie.indexOf(";")))
				makeRestCall(
						DataManager.getUserId(username).toObservable(),
						Consumer { response ->
							makeRestCall(
									DataManager.getUser(response.userId.id).toObservable(),
									Consumer { onUserResponse(it.user) }
							)
						}
				)
				return
			}
		}
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
					}.doOnSuccess { _ -> sendToView { it.onSuccessfullyLoggedIn() } }
							.subscribeOn(Schedulers.io())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe()
			)
		}
	}
}