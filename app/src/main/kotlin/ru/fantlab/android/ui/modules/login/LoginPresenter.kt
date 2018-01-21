package ru.fantlab.android.ui.modules.login

import ru.fantlab.android.data.dao.model.Login
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class LoginPresenter : BasePresenter<LoginMvp.View>(), LoginMvp.Presenter {

	override fun onUserResponse(response: Login?) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun login(username: String, password: String) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}