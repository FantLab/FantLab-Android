package ru.fantlab.android.ui.modules.login

import ru.fantlab.android.ui.base.mvp.BaseMvp

interface LoginMvp {

	interface View : BaseMvp.View {

		fun onEmptyUserName(isEmpty: Boolean)

		fun onEmptyPassword(isEmpty: Boolean)

		fun onSuccessfullyLoggedIn()
	}

	interface Presenter : BaseMvp.Presenter {

		fun proceedWithoutLogin()

		fun login(username: String, password: String)
	}
}