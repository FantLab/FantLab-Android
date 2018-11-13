package ru.fantlab.android.provider.theme

import android.app.Activity
import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.support.annotation.StyleRes
import ru.fantlab.android.R
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.helper.ViewHelper
import ru.fantlab.android.ui.base.BaseActivity

/**
 * Created by Kosh on 07 Jun 2017, 6:52 PM
 */

object ThemeEngine {

	fun apply(activity: BaseActivity<*, *>) {
		if (hasTheme(activity)) {
			return
		}
		val themeMode = PrefGetter.getThemeType(activity)
		val themeColor = PrefGetter.getThemeColor(activity)
		activity.setTheme(getTheme(themeMode, themeColor))
		setTaskDescription(activity)
		applyNavBarColor(activity)
	}

	private fun applyNavBarColor(activity: Activity) {
		if (!PrefGetter.isNavBarTintingDisabled() && PrefGetter.getThemeType() != PrefGetter.ThemeType.LIGHT) {
			activity.window.navigationBarColor = ViewHelper.getPrimaryColor(activity)
		}
	}

	/*fun applyForAbout(activity: MaterialAboutActivity) {
		val themeMode = PrefGetter.getThemeType(activity)
		when (themeMode) {
			PrefGetter.ThemeType.LIGHT -> activity.setTheme(R.style.AppTheme_AboutActivity_Light)
			PrefGetter.ThemeType.DARK -> activity.setTheme(R.style.AppTheme_AboutActivity_Dark)
		}
		setTaskDescription(activity)
	}*/

	fun applyDialogTheme(activity: BaseActivity<*, *>) {
		val themeMode = PrefGetter.getThemeType(activity)
		val themeColor = PrefGetter.getThemeColor(activity)
		activity.setTheme(getDialogTheme(themeMode, themeColor))
		setTaskDescription(activity)
	}

	@StyleRes
	private fun getTheme(themeType: PrefGetter.ThemeType, themeColor: PrefGetter.ThemeColor): Int {
		//Logger.e(themeType, themeColor)
		// I wish if I could simplify this :'( too many cases for the love of god.
		return when (themeType) {
			PrefGetter.ThemeType.LIGHT -> when (themeColor) {
				PrefGetter.ThemeColor.RED -> R.style.ThemeLight_Red
				PrefGetter.ThemeColor.PINK -> R.style.ThemeLight_Pink
				PrefGetter.ThemeColor.PURPLE -> R.style.ThemeLight_Purple
				PrefGetter.ThemeColor.DEEP_PURPLE -> R.style.ThemeLight_DeepPurple
				PrefGetter.ThemeColor.INDIGO -> R.style.ThemeLight_Indigo
				PrefGetter.ThemeColor.BLUE -> R.style.ThemeLight
				PrefGetter.ThemeColor.LIGHT_BLUE -> R.style.ThemeLight_LightBlue
				PrefGetter.ThemeColor.CYAN -> R.style.ThemeLight_Cyan
				PrefGetter.ThemeColor.TEAL -> R.style.ThemeLight_Teal
				PrefGetter.ThemeColor.GREEN -> R.style.ThemeLight_Green
				PrefGetter.ThemeColor.LIGHT_GREEN -> R.style.ThemeLight_LightGreen
				PrefGetter.ThemeColor.LIME -> R.style.ThemeLight_Lime
				PrefGetter.ThemeColor.YELLOW -> R.style.ThemeLight_Yellow
				PrefGetter.ThemeColor.AMBER -> R.style.ThemeLight_Amber
				PrefGetter.ThemeColor.ORANGE -> R.style.ThemeLight_Orange
				PrefGetter.ThemeColor.DEEP_ORANGE -> R.style.ThemeLight_DeepOrange
			}
			PrefGetter.ThemeType.DARK -> when (themeColor) {
				PrefGetter.ThemeColor.RED -> R.style.ThemeDark_Red
				PrefGetter.ThemeColor.PINK -> R.style.ThemeDark_Pink
				PrefGetter.ThemeColor.PURPLE -> R.style.ThemeDark_Purple
				PrefGetter.ThemeColor.DEEP_PURPLE -> R.style.ThemeDark_DeepPurple
				PrefGetter.ThemeColor.INDIGO -> R.style.ThemeDark_Indigo
				PrefGetter.ThemeColor.BLUE -> R.style.ThemeDark
				PrefGetter.ThemeColor.LIGHT_BLUE -> R.style.ThemeDark_LightBlue
				PrefGetter.ThemeColor.CYAN -> R.style.ThemeDark_Cyan
				PrefGetter.ThemeColor.GREEN -> R.style.ThemeDark_Green
				PrefGetter.ThemeColor.TEAL -> R.style.ThemeDark_Teal
				PrefGetter.ThemeColor.LIGHT_GREEN -> R.style.ThemeDark_LightGreen
				PrefGetter.ThemeColor.LIME -> R.style.ThemeDark_Lime
				PrefGetter.ThemeColor.YELLOW -> R.style.ThemeDark_Yellow
				PrefGetter.ThemeColor.AMBER -> R.style.ThemeDark_Amber
				PrefGetter.ThemeColor.ORANGE -> R.style.ThemeDark_Orange
				PrefGetter.ThemeColor.DEEP_ORANGE -> R.style.ThemeDark_DeepOrange
			}
		}
	}

	@StyleRes
	private fun getDialogTheme(themeType: PrefGetter.ThemeType, themeColor: PrefGetter.ThemeColor): Int {
		return when (themeType) {
			PrefGetter.ThemeType.LIGHT -> when (themeColor) {
				PrefGetter.ThemeColor.RED -> R.style.DialogThemeLight_Red
				PrefGetter.ThemeColor.PINK -> R.style.DialogThemeLight_Pink
				PrefGetter.ThemeColor.PURPLE -> R.style.DialogThemeLight_Purple
				PrefGetter.ThemeColor.DEEP_PURPLE -> R.style.DialogThemeLight_DeepPurple
				PrefGetter.ThemeColor.INDIGO -> R.style.DialogThemeLight_Indigo
				PrefGetter.ThemeColor.BLUE -> R.style.DialogThemeLight
				PrefGetter.ThemeColor.LIGHT_BLUE -> R.style.DialogThemeLight_LightBlue
				PrefGetter.ThemeColor.CYAN -> R.style.DialogThemeLight_Cyan
				PrefGetter.ThemeColor.TEAL -> R.style.DialogThemeLight_Teal
				PrefGetter.ThemeColor.GREEN -> R.style.DialogThemeLight_Green
				PrefGetter.ThemeColor.LIGHT_GREEN -> R.style.DialogThemeLight_LightGreen
				PrefGetter.ThemeColor.LIME -> R.style.DialogThemeLight_Lime
				PrefGetter.ThemeColor.YELLOW -> R.style.DialogThemeLight_Yellow
				PrefGetter.ThemeColor.AMBER -> R.style.DialogThemeLight_Amber
				PrefGetter.ThemeColor.ORANGE -> R.style.DialogThemeLight_Orange
				PrefGetter.ThemeColor.DEEP_ORANGE -> R.style.DialogThemeLight_DeepOrange
			}
			PrefGetter.ThemeType.DARK -> when (themeColor) {
				PrefGetter.ThemeColor.RED -> R.style.DialogThemeDark_Red
				PrefGetter.ThemeColor.PINK -> R.style.DialogThemeDark_Pink
				PrefGetter.ThemeColor.PURPLE -> R.style.DialogThemeDark_Purple
				PrefGetter.ThemeColor.DEEP_PURPLE -> R.style.DialogThemeDark_DeepPurple
				PrefGetter.ThemeColor.INDIGO -> R.style.DialogThemeDark_Indigo
				PrefGetter.ThemeColor.BLUE -> R.style.DialogThemeDark
				PrefGetter.ThemeColor.LIGHT_BLUE -> R.style.DialogThemeDark_LightBlue
				PrefGetter.ThemeColor.CYAN -> R.style.DialogThemeDark_Cyan
				PrefGetter.ThemeColor.TEAL -> R.style.DialogThemeDark_Teal
				PrefGetter.ThemeColor.GREEN -> R.style.DialogThemeDark_Green
				PrefGetter.ThemeColor.LIGHT_GREEN -> R.style.DialogThemeDark_LightGreen
				PrefGetter.ThemeColor.LIME -> R.style.DialogThemeDark_Lime
				PrefGetter.ThemeColor.YELLOW -> R.style.DialogThemeDark_Yellow
				PrefGetter.ThemeColor.AMBER -> R.style.DialogThemeDark_Amber
				PrefGetter.ThemeColor.ORANGE -> R.style.DialogThemeDark_Orange
				PrefGetter.ThemeColor.DEEP_ORANGE -> R.style.DialogThemeDark_DeepOrange
			}
		}
	}

	private fun setTaskDescription(activity: Activity) {
		activity.setTaskDescription(ActivityManager.TaskDescription(activity.getString(R.string.app_name),
				BitmapFactory.decodeResource(activity.resources, R.mipmap.ic_launcher), ViewHelper.getPrimaryColor(activity)))
	}

	private fun hasTheme(activity: BaseActivity<*, *>) = /*activity is LoginActivity*/false
}