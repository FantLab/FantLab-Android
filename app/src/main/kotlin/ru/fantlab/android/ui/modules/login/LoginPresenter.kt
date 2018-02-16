package ru.fantlab.android.ui.modules.login

import io.reactivex.functions.Consumer
import okhttp3.ResponseBody
import retrofit2.Response
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Login
import ru.fantlab.android.data.dao.model.saveLoggedUser
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.LoginProvider
import ru.fantlab.android.provider.rest.RestProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class LoginPresenter : BasePresenter<LoginMvp.View>(), LoginMvp.Presenter {

	override fun proceedWithoutLogin() {
		PrefGetter.setProceedWithoutLogin(true)
		sendToView { view -> view.onSuccessfullyLoggedIn() }
	}

	override fun login(username: String, password: String) {
		val usernameIsEmpty = InputHelper.isEmpty(username)
		val passwordIsEmpty = InputHelper.isEmpty(password)
		view?.onEmptyUserName(usernameIsEmpty)
		view?.onEmptyPassword(passwordIsEmpty)
		if (!usernameIsEmpty && !passwordIsEmpty) {
			try {
				makeRestCall(LoginProvider.getLoginRestService().login(username, password), Consumer { response ->
					onTokenResponse(username, response)
				})
			} catch (e: Exception) {
				sendToView { view -> view.showErrorMessage("The app was about to crash! (${e.message})") }
			}
		}
	}

	private fun onTokenResponse(username: String, response: Response<ResponseBody>) {
		response.headers().values("Set-Cookie").map {
			if (!InputHelper.isEmpty(it) && it.startsWith("fl_s")) {
				PrefGetter.setToken(it.substring(0, it.indexOf(";")))
				makeRestCall(RestProvider.getUserService().getLoggedUser(/*username*/58246), Consumer { response ->
					onUserResponse(response)
				})
				return
			}
		}
	}

	private fun onUserResponse(userModel: Login?) {
		if (userModel != null) {
			manageObservable(userModel.saveLoggedUser()
					.doOnComplete({
						PrefGetter.setProceedWithoutLogin(false)
						sendToView { view -> view.onSuccessfullyLoggedIn() }
					}))
			return
		} else {
			sendToView { view -> view.showMessage(R.string.error, R.string.failed_login) }
		}
	}
}