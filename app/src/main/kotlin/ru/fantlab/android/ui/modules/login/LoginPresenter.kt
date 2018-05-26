package ru.fantlab.android.ui.modules.login

import io.reactivex.functions.Consumer
import ru.fantlab.android.R
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
		view?.onEmptyUserName(usernameIsEmpty)
		view?.onEmptyPassword(passwordIsEmpty)
		if (!usernameIsEmpty && !passwordIsEmpty) {
			makeRestCall(
					DataManager.login(username, password)
							.map { it.first }
							.toObservable(),
					Consumer { response ->
						if (response.headers["Location"]?.get(0) == "/loginincorrect") {
							showSignInFailed()
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
						DataManager.getUserId(username)
								.map { it.get() }
								.toObservable(),
						Consumer { response ->
							makeRestCall(
									DataManager.getUser(response.userId.id)
											.map { it.get() }
											.toObservable(),
									Consumer {
										onUserResponse(it.user)
									}
							)
						}
				)
				return
			}
		}
	}

	private fun onUserResponse(user: User) {
		/*manageObservable(user.saveLoggedUser()
				.doOnComplete {
					PrefGetter.setProceedWithoutLogin(false)
					sendToView { it.onSuccessfullyLoggedIn() }
				}
		)*/
		sendToView { it.onSuccessfullyLoggedIn() }
		return
	}

	private fun showSignInFailed() {
		sendToView { it.showMessage(R.string.error, R.string.failed_login) }
	}
}