package ru.fantlab.android.helper

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.util.*
import android.content.Context.WINDOW_SERVICE
import android.provider.Settings
import android.view.WindowManager
import ru.fantlab.android.R


object AppHelper {

	fun hideKeyboard(view: View?) {
		val inputManager = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
		inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
	}

	fun getFragmentByTag(fragmentManager: FragmentManager, tag: String): Fragment? = fragmentManager.findFragmentByTag(tag)

	fun onShowHideFragment(fragmentManager: FragmentManager, toShow: Fragment, toHide: Fragment?) {
		toHide?.onHiddenChanged(true)
		fragmentManager
				.beginTransaction()
				.apply { if (toHide != null) hide(toHide) }
				.show(toShow)
				.commit()
		toShow.onHiddenChanged(false)
	}

	fun onAddAndHide(fragmentManager: FragmentManager, toAdd: Fragment, toHide: Fragment?) {
		toHide?.onHiddenChanged(true)
		fragmentManager
				.beginTransaction()
				.apply { if (toHide != null) hide(toHide) }
				.add(R.id.container, toAdd, toAdd.javaClass.simpleName)
				.addToBackStack(toAdd.javaClass.simpleName)
				.commit()
		toAdd.onHiddenChanged(false)
	}

	fun isNightMode(resources: Resources): Boolean {
		val themeType = PrefGetter.getThemeType(resources)
		return themeType != PrefGetter.LIGHT
	}

	fun updateAppLanguage(context: Context) {
		val lang = PrefGetter.getAppLanguage()
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			updateResources(context, lang)
		}
		updateResourcesLegacy(context, lang)
	}

	private fun updateResources(context: Context, language: String) {
		val locale = getLocale(language)
		Locale.setDefault(locale)
		val configuration = context.resources.configuration
		configuration.setLocale(locale)
		context.createConfigurationContext(configuration)
	}

	@Suppress("DEPRECATION")
	private fun updateResourcesLegacy(context: Context, language: String) {
		val locale = getLocale(language)
		Locale.setDefault(locale)
		val resources = context.resources
		val configuration = resources.configuration
		configuration.locale = locale
		resources.updateConfiguration(configuration, resources.displayMetrics)
	}

	private fun getLocale(language: String): Locale {
		var locale: Locale? = null
		if (language.equals("zh-rCN", ignoreCase = true)) {
			locale = Locale.SIMPLIFIED_CHINESE
		} else if (language.equals("zh-rTW", ignoreCase = true)) {
			locale = Locale.TRADITIONAL_CHINESE
		}
		if (locale != null) return locale
		val split = language.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		locale = if (split.size > 1) {
			Locale(split[0], split[1])
		} else {
			Locale(language)
		}
		return locale
	}

	@Suppress("DEPRECATION")
	fun updateAppFont(context: Context) {
		val appFontScale = PrefGetter.getAppFontScale()
		val resources = context.resources
		val configuration = resources.configuration
		val systemScale = Settings.System.getFloat(context.contentResolver, Settings.System.FONT_SCALE, 1f)

		configuration.fontScale = appFontScale*systemScale
		val metrics = context.resources.displayMetrics
		val wm = context.getSystemService(WINDOW_SERVICE) as WindowManager
		wm.defaultDisplay.getMetrics(metrics)
		metrics.scaledDensity = configuration.fontScale * metrics.density
		context.resources.updateConfiguration(configuration, metrics)
	}
}