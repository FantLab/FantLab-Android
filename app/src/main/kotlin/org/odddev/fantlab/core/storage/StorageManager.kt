package org.odddev.fantlab.core.storage

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class StorageManager(context: Context) {

	private val COOKIE_KEY = "COOKIE"
	private val USERNAME_KEY = "USERNAME"
	private val ANONYMOUS_KEY = "ANONYMOUS"

	private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

	fun saveCookie(cookie: String) {
		sharedPreferences.edit()
				.putString(COOKIE_KEY, cookie)
				.apply()
	}

	fun loadCookie(): String? = sharedPreferences.getString(COOKIE_KEY, null)

	fun clearCookie() {
		sharedPreferences.edit()
				.remove(COOKIE_KEY)
				.apply()
	}

	fun saveUsername(username: String) {
		sharedPreferences.edit()
				.putString(USERNAME_KEY, username)
				.apply()
	}

	fun loadUsername(): String? = sharedPreferences.getString(USERNAME_KEY, "Гость")

	fun clearUserName() {
		sharedPreferences.edit()
				.remove(USERNAME_KEY)
				.apply()
	}

	// todo разрулить схему с логином/разлогином получше
	fun saveAnonymus() {
		sharedPreferences.edit()
				.putBoolean(ANONYMOUS_KEY, true)
				.apply()
	}

	fun loadAnonymous(): Boolean = sharedPreferences.getBoolean(ANONYMOUS_KEY, false)

	fun clearAnonymous() {
		sharedPreferences.edit()
				.remove(ANONYMOUS_KEY)
				.apply()
	}
}
