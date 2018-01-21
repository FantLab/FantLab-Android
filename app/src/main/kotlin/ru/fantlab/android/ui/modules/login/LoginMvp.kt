package ru.fantlab.android.ui.modules.login

import ru.fantlab.android.data.dao.model.Login
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface LoginMvp {

	interface View : BaseMvp.View {

		fun onEmptyUserName(isEmpty: Boolean)

		fun onEmptyPassword(isEmpty: Boolean)

		fun onSuccessfullyLoggedIn(extraLogin: Boolean)
	}

	interface Presenter : BaseMvp.Presenter {

		//fun onTokenResponse(response: AccessTokenModel?)

		fun onUserResponse(response: Login?)

		fun login(username: String, password: String)
	}
}