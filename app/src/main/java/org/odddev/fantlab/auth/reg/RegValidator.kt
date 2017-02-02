package org.odddev.fantlab.auth.reg

import android.content.Context
import android.content.res.Resources
import android.util.Patterns

import org.odddev.fantlab.R
import org.odddev.fantlab.core.validation.Validator

/**
 * @author kenrube
 * *
 * @since 18.09.16
 */

class RegValidator internal constructor(context: Context) : Validator() {

	companion object {

		const val USERNAME = 0
		const val PASSWORD = 1
		const val EMAIL = 2
		const val BIRTH_DATE = 3
		const val LOCATION_STRING = 4
		const val WEB_PAGE = 5
		const val SKYPE = 6
		const val ICQ = 7
	}

	private val resources: Resources = context.resources

	internal var birthDay: Int = 0
	internal var birthMonth: Int = 0
	internal var birthYear: Int = 0

	override fun validate(field: Int) {
		val value = fields[field]
		fieldErrors.put(field, getFieldError(field, value))
	}

	public override fun areFieldsValid(): Boolean {
		var result = true
		for (i in 0..7) {
			validate(i)
			if (fieldErrors[i] != null) result = false
		}
		return result
	}

	private fun getFieldError(field: Int, value: String?): String? {
		when (field) {
			USERNAME -> return if (value == null || value.trim { it <= ' ' }.isEmpty())
				resources.getString(R.string.auth_username_empty)
			else
				null
			PASSWORD -> return if (value == null || value.trim { it <= ' ' }.isEmpty())
				resources.getString(R.string.auth_password_empty)
			else
				null
			EMAIL -> return if (value == null || !Patterns.EMAIL_ADDRESS.matcher(value).matches())
				resources.getString(R.string.register_email_incorrect)
			else
				null
			BIRTH_DATE -> return null
			LOCATION_STRING -> return null
			WEB_PAGE -> return if (value == null || !Patterns.WEB_URL.matcher(value).matches())
				resources.getString(R.string.register_url_incorrect)
			else
				null
			SKYPE -> return null
			ICQ -> return if (value == null || value.length < 5 || value.length > 9)
				resources.getString(R.string.register_icq_incorrect)
			else
				null
			else -> return null
		}
	}
}
