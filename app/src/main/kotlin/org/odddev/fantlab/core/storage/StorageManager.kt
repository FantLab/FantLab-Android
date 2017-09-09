package org.odddev.fantlab.core.storage

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class StorageManager(context: Context) {

	private val COOKIE_KEY = "COOKIE"
	private val USERNAME_KEY = "USERNAME"

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

	fun saveUsername(username: String) {
		val editor = sharedPreferences.edit()
		editor.putString(USERNAME_KEY, username)
		editor.apply()
	}

	fun loadUsername(): String? = sharedPreferences.getString(USERNAME_KEY, "Гость")

	fun clearUserName() {
		val editor = sharedPreferences.edit()
		editor.remove(USERNAME_KEY)
		editor.apply()
	}
}
