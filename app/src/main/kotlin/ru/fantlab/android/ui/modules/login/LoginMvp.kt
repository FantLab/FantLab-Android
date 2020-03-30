package ru.fantlab.android.ui.modules.login

import ru.fantlab.android.ui.base.mvp.BaseMvp

interface LoginMvp {

	interface View : BaseMvp.View {

		fun onEmptyUserName(isEmpty: Boolean)

		fun onEmptyPassword(isEmpty: Boolean)

		fun onSuccessfullyLoggedIn()

		fun showSignInFailed()

		fun showSignInSecondFailed()

		fun onWrongUsername()

		fun onWrongPassword()

		fun showUserBlocked(endDate: String)

		fun showUserBlockedForever()
	}

	interface Presenter : BaseMvp.Presenter {

		fun proceedWithoutLogin()

		fun login(username: String, password: String)
	}
}