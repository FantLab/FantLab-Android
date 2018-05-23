package ru.fantlab.android.ui.modules.login

import io.reactivex.functions.Consumer
import okhttp3.ResponseBody
import retrofit2.Response
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.newmodel.User
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.LoginProvider
import ru.fantlab.android.provider.rest.RestProvider
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
			try {
				makeRestCall(LoginProvider.getLoginRestService().login(username, password), Consumer {
					val location = it.headers().values("location")
					if (location.isNotEmpty() && location[0] == "/loginincorrect") {
						showSignInFailed()
						return@Consumer
					}
					onTokenResponse(username, it)
				})
			} catch (e: Exception) {
				sendToView { it.showErrorMessage("The app was about to crash! (${e.message})") }
			}
		}
	}

	private fun onTokenResponse(username: String, response: Response<ResponseBody>) {
		response.headers().values("Set-Cookie").map {
			if (!InputHelper.isEmpty(it) && it.startsWith("fl_s")) {
				PrefGetter.setToken(it.substring(0, it.indexOf(";")))
				makeRestCall(RestProvider.getUserService().getUserId(username), Consumer {
					if (it.userId == 0) {
						// по идее, здесь мы оказаться не можем ни в коем случае
						showSignInFailed()
						return@Consumer
					}
					makeRestCall(
							DataManager.getUser(it.userId)
									.map { it.get() }
									.toObservable(),
							Consumer {
								onUserResponse(it.user)
							}
					)
				})
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
		return
	}

	private fun showSignInFailed() {
		sendToView { it.showMessage(R.string.error, R.string.failed_login) }
	}
}