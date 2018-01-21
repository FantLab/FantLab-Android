package ru.fantlab.android.ui.modules.login

import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.ProgressBar
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.ui.base.BaseActivity

class LoginActivity : BaseActivity<LoginMvp.View, LoginPresenter>(), LoginMvp.View {

	@JvmField
	@BindView(R.id.usernameEditText)
	var usernameEditText: TextInputEditText? = null

	@JvmField
	@BindView(R.id.username)
	var username: TextInputLayout? = null

	@JvmField
	@BindView(R.id.passwordEditText)
	var passwordEditText: TextInputEditText? = null

	@JvmField
	@BindView(R.id.password)
	var password: TextInputLayout? = null

	@JvmField
	@BindView(R.id.login)
	var login: FloatingActionButton? = null

	@JvmField
	@BindView(R.id.progress)
	var progress: ProgressBar? = null

	override fun isTransparent(): Boolean = true

	override fun providePresenter(): LoginPresenter = LoginPresenter()

	override fun layout(): Int = R.layout.login_form_layout

	override fun canBack(): Boolean = false

	override fun isSecured(): Boolean = true

	override fun onEmptyUserName(isEmpty: Boolean) {
		username?.error = if (isEmpty) getString(R.string.required_field) else null
	}

	override fun onEmptyPassword(isEmpty: Boolean) {
		password?.error = if (isEmpty) getString(R.string.required_field) else null
	}

	override fun onSuccessfullyLoggedIn(extraLogin: Boolean) {
		hideProgress()
		onRestartApp()
	}

	override fun hideProgress() {
		progress?.visibility = View.GONE
		login?.show()
	}
}