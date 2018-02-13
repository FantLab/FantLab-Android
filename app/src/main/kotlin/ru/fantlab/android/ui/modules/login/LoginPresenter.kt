package ru.fantlab.android.ui.modules.login

import io.reactivex.functions.Consumer
import okhttp3.ResponseBody
import retrofit2.Response
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Login
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.LoginProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class LoginPresenter : BasePresenter<LoginMvp.View>(), LoginMvp.Presenter {

	override fun login(username: String, password: String) {
		val usernameIsEmpty = InputHelper.isEmpty(username)
		val passwordIsEmpty = InputHelper.isEmpty(password)
		if (view == null) return
		view?.onEmptyUserName(usernameIsEmpty)
		view?.onEmptyPassword(passwordIsEmpty)
		if (!usernameIsEmpty && !passwordIsEmpty) {
			try {
				makeRestCall(LoginProvider.getLoginRestService().login(username, password), Consumer { response ->
					onTokenResponse(response)
				})
			} catch (e: Exception) {
				sendToView { view -> view.showErrorMessage("The app was about to crash!!(${e.message})") }
			}
		}
	}

	private fun onTokenResponse(response: Response<ResponseBody>) {
		response.headers().values("Set-Cookie").map {
			if (!InputHelper.isEmpty(it) && it.startsWith("fl_s")) {
				PrefGetter.setToken(it)
				// todo дернуть запрос на получение данных юзера
				//makeRestCall(RestProvider.getUserService(false).getUser(), ???({ this.onUserResponse(it) }))
				onUserResponse(null)
				return
			}
		}
		sendToView { view -> view.showMessage(R.string.error, R.string.failed_login) }
	}

	private fun onUserResponse(response: Login?) {
		sendToView { view -> view.onSuccessfullyLoggedIn() }
	}
}