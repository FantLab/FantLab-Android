package org.odddev.fantlab.auth.login

import android.content.Context
import android.content.res.Resources

import org.odddev.fantlab.R
import org.odddev.fantlab.core.validation.Validator

/**
 * @author kenrube
 * *
 * @since 17.09.16
 */

class LoginValidator(context: Context) : Validator() {

	companion object {

		const val USERNAME = 0
		const val PASSWORD = 1
	}

	private val resources: Resources = context.resources

	override fun validate(field: Int) {
		val value = fields[field]
		when (field) {
			USERNAME -> {
				fieldErrors[USERNAME] = getUsernameError(value)
			}
			PASSWORD -> {
				fieldErrors[PASSWORD] = getPasswordError(value)
			}
		}
	}

	public override fun areFieldsValid(): Boolean {
		var result = true
		for (i in 0..1) {
			validate(i)
			if (fieldErrors[i] != null) result = false
		}
		return result
	}

	private fun getUsernameError(username: String?): String? {
		return if (username == null || username.trim { it <= ' ' }.isEmpty())
			resources.getString(R.string.auth_username_empty)
		else
			null
	}

	private fun getPasswordError(password: String?): String? {
		return if (password == null || password.trim { it <= ' ' }.isEmpty())
			resources.getString(R.string.auth_password_empty)
		else
			null
	}
}
