package org.odddev.fantlab.core.storage

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * @author kenrube
 * *
 * @since 29.09.16
 */

class StorageManager(context: Context) {

	private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

	fun saveCookie(cookie: String) {
		val editor = sharedPreferences.edit()
		editor.putString(COOKIE_KEY, cookie)
		editor.apply()
	}

	fun loadCookie(): String? = sharedPreferences.getString(COOKIE_KEY, null)

	fun clearCookie() {
		val editor = sharedPreferences.edit()
		editor.remove(COOKIE_KEY)
		editor.apply()
	}

	// todo 6.32
	fun saveUsername(username: String) {
		val editor = sharedPreferences.edit()
		editor.putString(USERNAME_KEY, username)
		editor.apply()
	}

	fun loadUsername(): String? = sharedPreferences.getString(USERNAME_KEY, "Guest")

	companion object {

		private val COOKIE_KEY = "COOKIE"
		private val USERNAME_KEY = "USERNAME"
	}
}
