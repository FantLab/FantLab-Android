package org.odddev.fantlab.auth.login

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter

import org.odddev.fantlab.R
import org.odddev.fantlab.auth.AuthActivity
import org.odddev.fantlab.auth.AuthRouter
import org.odddev.fantlab.databinding.LoginFragmentBinding

/**
 * @author kenrube
 * *
 * @since 30.08.16
 */

class LoginFragment : MvpAppCompatFragment(), ILoginView, ILoginActions {

	@InjectPresenter
	lateinit var presenter: LoginPresenter

	private lateinit var binding: LoginFragmentBinding
	private lateinit var router: AuthRouter

	private lateinit var loginValidator: LoginValidator

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		router = AuthRouter(activity as AuthActivity, R.id.container)
	}

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = LoginFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		binding.handler = this
		loginValidator = LoginValidator(context)
		binding.loginValidator = loginValidator
		binding.forgotPass = false
	}

	override fun login() {
		presenter.login(loginValidator)
	}

	override fun register() {
		router.routeToReg()
	}

	override fun forgotPass() {
		// todo 3.1, 3.2, 3.3
	}

	override fun entry() {
		router.routeToHome(false)
	}

	override fun showFieldsInvalid() {
		binding.forgotPass = false
	}

	override fun showLoginResult(loggedIn: Boolean) {
		if (loggedIn) {
			router.routeToHome(true)
		} else {
			binding.forgotPass = true
			showError(getString(R.string.auth_login_error))
		}
	}

	override fun showError(error: String) {
		Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
	}
	// todo 1.10
}
