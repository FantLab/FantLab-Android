package ru.fantlab.android.helper

import android.content.res.Resources
import ru.fantlab.android.R

object PrefGetter {

	enum class ThemeType {
		LIGHT,
		DARK
	}

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

	fun isAppAnimationDisabled(): Boolean {
		return PrefHelper.getBoolean("app_animation")
	}
}
