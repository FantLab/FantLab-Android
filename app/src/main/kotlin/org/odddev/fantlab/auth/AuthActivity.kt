package org.odddev.fantlab.auth

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import org.odddev.fantlab.R
import android.text.Editable
import android.text.TextWatcher
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import org.odddev.fantlab.databinding.AuthActivityBinding
import org.odddev.fantlab.home.HomeActivity


class AuthActivity : MvpAppCompatActivity(), IAuthView {

	@InjectPresenter
	lateinit var presenter: AuthPresenter

	private val binding: AuthActivityBinding by lazy {
		DataBindingUtil.setContentView<AuthActivityBinding>(this, R.layout.auth_activity)
	}

	private val authValidator: AuthValidator by lazy { AuthValidator(this) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding.authValidator = authValidator

		binding.usernameEdit.setOnFocusChangeListener { _, hasFocus ->
			run {
				if (!hasFocus) {
					binding.usernameEdit.isSelected = binding.usernameEdit.text.isNotEmpty()
				}
			}
		}

		binding.passwordEdit.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
			}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			}
			override fun afterTextChanged(s: Editable) {
				binding.password.isPasswordVisibilityToggleEnabled = s.isNotEmpty()
			}
		})
		binding.passwordEdit.setOnFocusChangeListener { _, hasFocus ->
			run {
				if (!hasFocus) {
					binding.passwordEdit.isSelected = binding.passwordEdit.text.isNotEmpty()
				}
			}
		}

		binding.login.setOnClickListener {_ -> presenter.login(authValidator) }

		binding.entry.setOnClickListener {_ ->
			run {
				val intent = Intent(this, HomeActivity::class.java)
				intent.putExtra("LOGGED_IN", false)
				startActivity(intent)
				finish()
			}
		}
	}

	override fun showAuthResult(loggedIn: Boolean) {
		if (loggedIn) {
			val intent = Intent(this, HomeActivity::class.java)
			intent.putExtra("LOGGED_IN", true)
			startActivity(intent)
			finish()
		} else {
			showError(getString(R.string.auth_login_error))
		}
	}

	override fun showError(error: String) {
		Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
	}

	override fun showFieldsInvalid() {
		Snackbar.make(binding.root, "Неверные логин/пароль", Snackbar.LENGTH_LONG).show()
	}
}