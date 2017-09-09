package org.odddev.fantlab.auth

import android.content.Context
import android.content.res.Resources
import org.odddev.fantlab.R
import org.odddev.fantlab.core.validation.Validator

class AuthValidator(context: Context) : Validator() {

	companion object {

		const val USERNAME = 0
		const val PASSWORD = 1
	}

	private val resources: Resources by lazy { context.resources }

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
		(USERNAME..PASSWORD).map {
			validate(it)
			if (fieldErrors[it] != null) result = false
		}
		return result
	}

	private fun getUsernameError(username: String?): String? =
		if (username.isNullOrBlank()) resources.getString(R.string.auth_username_empty)
		else null

	private fun getPasswordError(password: String?): String? =
		if (password.isNullOrBlank()) resources.getString(R.string.auth_password_empty)
		else null
}