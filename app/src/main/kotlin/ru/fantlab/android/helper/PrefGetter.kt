package ru.fantlab.android.helper

import android.content.Context
import android.content.res.Resources
import ru.fantlab.android.App
import ru.fantlab.android.BuildConfig
import ru.fantlab.android.R

object PrefGetter {

	enum class ThemeType {
		LIGHT,
		DARK
	}

	enum class ThemeColor {
		RED,
		PINK,
		PURPLE,
		DEEP_PURPLE,
		INDIGO,
		BLUE,
		LIGHT_BLUE,
		CYAN,
		TEAL,
		GREEN,
		LIGHT_GREEN,
		LIME,
		YELLOW,
		AMBER,
		ORANGE,
		DEEP_ORANGE
	}

	private val TOKEN = "token"
	private val WHATS_NEW_VERSION = "whats_new"
	private val APP_LANGUAGE = "app_language"
	private val NAV_DRAWER_GUIDE = "nav_drawer_guide"

	fun setToken(token: String?) {
		PrefHelper[TOKEN] = token
	}

	fun getThemeType(): ThemeType {
		return getThemeType(App.instance.resources)
	}

	fun getThemeType(context: Context): ThemeType = getThemeType(context.resources)

	fun getThemeType(resources: Resources): PrefGetter.ThemeType {
		val appTheme = PrefHelper.getString("appTheme")
		if (!appTheme.isNullOrEmpty()) {
			return when {
				appTheme.equals(resources.getString(R.string.dark_theme_mode), true) -> PrefGetter.ThemeType.DARK
				appTheme.equals(resources.getString(R.string.light_theme_mode), true) -> PrefGetter.ThemeType.LIGHT
				else -> PrefGetter.ThemeType.LIGHT
			}
		}
		return PrefGetter.ThemeType.LIGHT
	}

	fun getThemeColor(context: Context): ThemeColor = getThemeColor(context.resources)

	private fun getThemeColor(resources: Resources): ThemeColor {
		val appColor = PrefHelper.getString("appColor")
		return getThemeColor(resources, appColor)
	}

	// used for color picker to get the color (enum) from the name of the color
	fun getThemeColor(resources: Resources, appColor: String?): ThemeColor {
		if (!InputHelper.isEmpty(appColor)) {
			return when {
				appColor.equals(resources.getString(R.string.red_theme_mode), ignoreCase = true) -> ThemeColor.RED
				appColor.equals(resources.getString(R.string.pink_theme_mode), ignoreCase = true) -> ThemeColor.PINK
				appColor.equals(resources.getString(R.string.purple_theme_mode), ignoreCase = true) -> ThemeColor.PURPLE
				appColor.equals(resources.getString(R.string.deep_purple_theme_mode), ignoreCase = true) -> ThemeColor.DEEP_PURPLE
				appColor.equals(resources.getString(R.string.indigo_theme_mode), ignoreCase = true) -> ThemeColor.INDIGO
				appColor.equals(resources.getString(R.string.blue_theme_mode), ignoreCase = true) -> ThemeColor.BLUE
				appColor.equals(resources.getString(R.string.light_blue_theme_mode), ignoreCase = true) -> ThemeColor.LIGHT_BLUE
				appColor.equals(resources.getString(R.string.cyan_theme_mode), ignoreCase = true) -> ThemeColor.CYAN
				appColor.equals(resources.getString(R.string.teal_theme_mode), ignoreCase = true) -> ThemeColor.TEAL
				appColor.equals(resources.getString(R.string.green_theme_mode), ignoreCase = true) -> ThemeColor.GREEN
				appColor.equals(resources.getString(R.string.light_green_theme_mode), ignoreCase = true) -> ThemeColor.LIGHT_GREEN
				appColor.equals(resources.getString(R.string.lime_theme_mode), ignoreCase = true) -> ThemeColor.LIME
				appColor.equals(resources.getString(R.string.yellow_theme_mode), ignoreCase = true) -> ThemeColor.YELLOW
				appColor.equals(resources.getString(R.string.amber_theme_mode), ignoreCase = true) -> ThemeColor.AMBER
				appColor.equals(resources.getString(R.string.orange_theme_mode), ignoreCase = true) -> ThemeColor.ORANGE
				appColor.equals(resources.getString(R.string.deep_orange_theme_mode), ignoreCase = true) -> ThemeColor.DEEP_ORANGE
				else -> ThemeColor.BLUE
			}
		}
		return ThemeColor.BLUE
	}

	fun isAppAnimationDisabled(): Boolean {
		return PrefHelper.getBoolean("app_animation")
	}

	fun getAppLanguage(): String {
		val appLanguage = PrefHelper.getString(APP_LANGUAGE)
		return appLanguage ?: "en"
	}

	fun showWhatsNew(): Boolean {
		return PrefHelper.getInt(WHATS_NEW_VERSION) != BuildConfig.VERSION_CODE
	}

	fun isNavDrawerHintShowed(): Boolean {
		val isShowed = PrefHelper.getBoolean(NAV_DRAWER_GUIDE)
		PrefHelper[NAV_DRAWER_GUIDE] = true
		return isShowed
	}

	fun isTwiceBackButtonDisabled(): Boolean {
		return PrefHelper.getBoolean("back_button")
	}

	fun isNavBarTintingDisabled(): Boolean {
		return PrefHelper.getBoolean("navigation_color")
	}

	fun isRectAvatar(): Boolean {
		return PrefHelper.getBoolean("rect_avatar")
	}

	fun isRVAnimationEnabled(): Boolean {
		return PrefHelper.getBoolean("recylerViewAnimation")
	}
}
