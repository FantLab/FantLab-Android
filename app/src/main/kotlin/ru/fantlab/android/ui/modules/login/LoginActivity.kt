package ru.fantlab.android.ui.modules.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnEditorAction
import ru.fantlab.android.R
import ru.fantlab.android.helper.AnimHelper
import ru.fantlab.android.helper.AppHelper
import ru.fantlab.android.helper.InputHelper
import ru.fantlab.android.ui.base.BaseActivity

class LoginActivity : BaseActivity<LoginMvp.View, LoginPresenter>(), LoginMvp.View {

	@BindView(R.id.usernameEditText)
	lateinit var usernameEditText: TextInputEditText

	@BindView(R.id.username)
	lateinit var username: TextInputLayout

	@BindView(R.id.passwordEditText)
	lateinit var passwordEditText: TextInputEditText

	@BindView(R.id.password)
	lateinit var password: TextInputLayout

	@BindView(R.id.login)
	lateinit var login: FloatingActionButton

	@BindView(R.id.progress)
	lateinit var progress: ProgressBar

	override fun onCreate(savedInstanceState: Bundle?) {
		setTheme(R.style.LoginTheme)
		super.onCreate(savedInstanceState)
	}

	override fun isTransparent(): Boolean = true

	override fun providePresenter(): LoginPresenter = LoginPresenter()

	override fun layout(): Int = R.layout.login_form_layout

	override fun canBack(): Boolean = false

	override fun isSecured(): Boolean = true

	@OnClick(R.id.login)
	fun onClick() {
		doLogin()
	}

	@OnEditorAction(R.id.passwordEditText)
	fun onSendPassword(): Boolean {
		doLogin()
		return true
	}

	override fun onEmptyUserName(isEmpty: Boolean) {
		username.error = if (isEmpty) getString(R.string.required_field) else null
	}

	override fun onEmptyPassword(isEmpty: Boolean) {
		password.error = if (isEmpty) getString(R.string.required_field) else null
	}

	override fun onSuccessfullyLoggedIn() {
		hideProgress()
		onRestartApp()
	}

	override fun hideProgress() {
		progress.visibility = View.GONE
		login.show()
	}

	override fun showErrorMessage(msgRes: String) {
		hideProgress()
		super.showErrorMessage(msgRes)
	}

	override fun showMessage(@StringRes titleRes: Int, @StringRes msgRes: Int) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	override fun showMessage(titleRes: String, msgRes: String) {
		hideProgress()
		super.showMessage(titleRes, msgRes)
	}

	override fun showProgress(@StringRes resId: Int, cancelable: Boolean) {
		login.hide()
		AppHelper.hideKeyboard(login)
		AnimHelper.animateVisibility(progress, true)
	}

	private fun doLogin() {
		if (progress.visibility == View.GONE) {
			presenter.login(InputHelper.toString(username), InputHelper.toString(password))
		}
	}

	companion object {

		fun start(activity: Activity) {
			val intent = Intent(activity, LoginActivity::class.java)
			intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
			activity.startActivity(intent)
			activity.finish()
		}
	}
}