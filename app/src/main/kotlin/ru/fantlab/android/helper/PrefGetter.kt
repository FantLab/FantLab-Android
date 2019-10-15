package ru.fantlab.android.helper

import android.content.Context
import android.content.res.Resources
import ru.fantlab.android.App
import ru.fantlab.android.BuildConfig
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.provider.rest.DataManager

object PrefGetter {

	val RED = 1
	val PINK = 2
	val PURPLE = 3
	val DEEP_PURPLE = 4
	val INDIGO = 5
	val BLUE = 6
	val LIGHT_BLUE = 7
	val CYAN = 8
	val TEAL = 9
	val GREEN = 10
	val LIGHT_GREEN = 11
	val LIME = 12
	val YELLOW = 13
	val AMBER = 14
	val ORANGE = 15
	val DEEP_ORANGE = 16

	val LIGHT = 1
	val DARK = 2
	val AMLOD = 3
	val BLUISH = 4
	val MID_NIGHT_BLUE = 5

	private const val TOKEN = "token"
	private const val WHATS_NEW_VERSION = "whats_new"
	private const val APP_LANGUAGE = "app_language"
	private const val NAV_DRAWER_GUIDE = "nav_drawer_guide"
	private const val LOGGED_USER = "logged_user"
	private const val APP_FONT_SCALE = "app_font_scale"

	fun setLoggedUser(user: User) {
		PrefHelper[LOGGED_USER] = DataManager.gson.toJson(user)
	}

	fun getLoggedUser(): User? {
		val user = PrefHelper.getString(LOGGED_USER)
		return DataManager.gson.fromJson(user, User::class.java)
	}

	fun getSessionUserId(): Int {
		return FantlabHelper.currentUserId
	}

	fun setSessionUserId() {
		FantlabHelper.currentUserId = getLoggedUser()?.id ?: -1
	}

	fun clearLoggedUser() {
		PrefHelper.clearKey(LOGGED_USER)
	}

	fun setProceedWithoutLogin(proceed: Boolean) {
		PrefHelper["proceed_without_login"] = proceed
	}

	fun proceedWithoutLogin(): Boolean = PrefHelper.getBoolean("proceed_without_login")

	fun setToken(token: String?) {
		PrefHelper[TOKEN] = token
	}

	fun getToken(): String? {
		return PrefHelper.getString(TOKEN)
	}

	fun getThemeType(): Int {
		return getThemeType(App.instance.resources)
	}

	fun getThemeType(context: Context): Int = getThemeType(context.resources)

	fun getThemeType(resources: Resources): Int {
		val appTheme = PrefHelper.getString("appTheme")
		if (!InputHelper.isEmpty(appTheme)) {
			when {
				appTheme.equals(resources.getString(R.string.dark_theme_mode)) -> return DARK
				appTheme.equals(resources.getString(R.string.light_theme_mode)) -> return LIGHT
				appTheme.equals(resources.getString(R.string.amlod_theme_mode)) -> return AMLOD
				appTheme.equals(resources.getString(R.string.mid_night_blue_theme_mode)) -> return MID_NIGHT_BLUE
				appTheme.equals(resources.getString(R.string.bluish_theme)) -> return BLUISH
			}
		}
		return LIGHT
	}

	fun getThemeColor(context: Context): Int = getThemeColor(context.resources)

	private fun getThemeColor(resources: Resources): Int {
		val appColor = PrefHelper.getString("appColor")
		return getThemeColor(resources, appColor)
	}

	fun getThemeColor(resources: Resources, appColor: String?): Int {
		if (!InputHelper.isEmpty(appColor)) {
			if (appColor.equals(resources.getString(R.string.red_theme_mode), ignoreCase = true))
				return RED
			if (appColor.equals(resources.getString(R.string.pink_theme_mode), ignoreCase = true))
				return PINK.toInt()
			if (appColor.equals(resources.getString(R.string.purple_theme_mode), ignoreCase = true))
				return PURPLE
			if (appColor.equals(resources.getString(R.string.deep_purple_theme_mode), ignoreCase = true))
				return DEEP_PURPLE
			if (appColor.equals(resources.getString(R.string.indigo_theme_mode), ignoreCase = true))
				return INDIGO
			if (appColor.equals(resources.getString(R.string.blue_theme_mode), ignoreCase = true))
				return BLUE
			if (appColor.equals(resources.getString(R.string.light_blue_theme_mode), ignoreCase = true))
				return LIGHT_BLUE
			if (appColor.equals(resources.getString(R.string.cyan_theme_mode), ignoreCase = true))
				return CYAN
			if (appColor.equals(resources.getString(R.string.teal_theme_mode), ignoreCase = true))
				return TEAL
			if (appColor.equals(resources.getString(R.string.green_theme_mode), ignoreCase = true))
				return GREEN
			if (appColor.equals(resources.getString(R.string.light_green_theme_mode), ignoreCase = true))
				return LIGHT_GREEN
			if (appColor.equals(resources.getString(R.string.lime_theme_mode), ignoreCase = true))
				return LIME
			if (appColor.equals(resources.getString(R.string.yellow_theme_mode), ignoreCase = true))
				return YELLOW
			if (appColor.equals(resources.getString(R.string.amber_theme_mode), ignoreCase = true))
				return AMBER
			if (appColor.equals(resources.getString(R.string.orange_theme_mode), ignoreCase = true))
				return ORANGE
			if (appColor.equals(resources.getString(R.string.deep_orange_theme_mode), ignoreCase = true))
				return DEEP_ORANGE
		}
		return BLUE
	}

	fun getAppLanguage(): String {
		val appLanguage = PrefHelper.getString(APP_LANGUAGE)
		return appLanguage ?: "ru"
	}

	fun setAppLanguage(language: String?) {
		PrefHelper[APP_LANGUAGE] = language ?: "ru"
	}

	fun showWhatsNew(): Boolean {
		return PrefHelper.getInt(WHATS_NEW_VERSION) != BuildConfig.VERSION_CODE
	}

	fun isNavDrawerHintShowed(): Boolean {
		val isShowed = PrefHelper.getBoolean(NAV_DRAWER_GUIDE)
		PrefHelper[NAV_DRAWER_GUIDE] = true
		return isShowed
	}

	fun isTwiceBackButtonEnabled(): Boolean {
		return PrefHelper.getBoolean("back_button")
	}

	fun isNavBarTintingEnabled(): Boolean {
		return PrefHelper.getBoolean("navigation_color")
	}

	fun isRVAnimationEnabled(): Boolean {
		return PrefHelper.getBoolean("recylerViewAnimation")
	}

	fun isForumExtended(): Boolean {
		return PrefHelper.getBoolean("forumExtended")
	}

	fun getTopicMessagesOrder(): String {
		val order = PrefHelper.getString("topicOrder")
		return order ?: "desc"
	}
  
  	fun setAppFontScale(value: Float) {
		PrefHelper[APP_FONT_SCALE] = value
	}

	fun getAppFontScale(): Float {
		val fontScale = PrefHelper.getFloat(APP_FONT_SCALE)
		return if (fontScale == 0f) 1f else fontScale
	}
}
