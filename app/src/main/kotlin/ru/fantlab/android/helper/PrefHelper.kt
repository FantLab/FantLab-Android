package ru.fantlab.android.helper

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.preference.PreferenceManager
import ru.fantlab.android.App

/**
 * Created by kosh20111 on 19 Feb 2017, 2:01 AM
 */
object PrefHelper {

	/**
	 * @param key
	 * ( the Key to used to retrieve this data later  )
	 * @param value
	 * ( any kind of primitive values  )
	 *
	 *
	 * non can be null!!!
	 */
	@SuppressLint("ApplySharedPref")
	operator fun <T> set(key: String, value: T?) {
		if (key.isBlank()) {
			throw NullPointerException("Key must not be null! (key = $key), (value = $value)")
		}
		val edit = PreferenceManager.getDefaultSharedPreferences(App.instance).edit()
		if (value == null || value.toString().isEmpty()) {
			clearKey(key)
			return
		}
		when (value) {
			is String -> edit.putString(key, value as String)
			is Int -> edit.putInt(key, (value as Int))
			is Long -> edit.putLong(key, (value as Long))
			is Boolean -> edit.putBoolean(key, (value as Boolean))
			is Float -> edit.putFloat(key, (value as Float))
			else -> edit.putString(key, value.toString())
		}
		edit.commit()//apply on UI
	}

	private fun getPrefs(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.instance)

	fun getString(key: String): String? = getPrefs().getString(key, null)

	fun getBoolean(key: String): Boolean = getPrefs().all[key] is Boolean && getPrefs().getBoolean(key, false)

	fun getInt(key: String): Int = if (getPrefs().all[key] is Int) getPrefs().getInt(key, 0) else -1

	fun getLong(key: String): Long = getPrefs().getLong(key, 0)

	fun getFloat(key: String): Float = getPrefs().getFloat(key, 0f)

	fun clearKey(key: String) = getPrefs().edit().remove(key).apply()


	fun isExist(key: String): Boolean = getPrefs().contains(key)

	fun clearPrefs() = getPrefs().edit().clear().apply()

	fun getAll(): Map<String, *> = getPrefs().all
}