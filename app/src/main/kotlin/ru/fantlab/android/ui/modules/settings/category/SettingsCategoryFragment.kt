package ru.fantlab.android.ui.modules.settings.category

import android.content.Context
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.widget.Toast

import es.dmoral.toasty.Toasty
import io.reactivex.disposables.CompositeDisposable
import ru.fantlab.android.App
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.SettingsModel
import ru.fantlab.android.helper.PrefHelper
import ru.fantlab.android.ui.base.mvp.BaseMvp

class SettingsCategoryFragment : PreferenceFragmentCompat(), android.support.v7.preference.Preference.OnPreferenceChangeListener {
	private var callback: BaseMvp.View? = null
	private var appColor: String? = null
	private var appLanguage: String? = null
	private var settingsCallback: SettingsCallback? = null
	private val disposable = CompositeDisposable()

	interface SettingsCallback {
		fun getSettingsType(): Int
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		this.callback = context as BaseMvp.View?
		this.settingsCallback = context as SettingsCallback?
		appColor = PrefHelper.getString("appColor")
		appLanguage = PrefHelper.getString("app_language")
	}

	override fun onDetach() {
		callback = null
		settingsCallback = null
		super.onDetach()
	}

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

		when (settingsCallback!!.getSettingsType()) {
			SettingsModel.CUSTOMIZATION -> addCustomization()
			else -> Toast.makeText(App.instance, "Ошибка", Toast.LENGTH_SHORT).show()
		}
	}

	override fun onPreferenceChange(preference: android.support.v7.preference.Preference, newValue: Any?): Boolean {
		if (preference.key.equals("recylerViewAnimation", ignoreCase = true)) {
			callback!!.onThemeChanged()
			return true
		} else if (preference.key.equals("appColor", ignoreCase = true)) {
			if (newValue.toString().equals(appColor, ignoreCase = true))
				return true
			Toasty.warning(App.instance, getString(R.string.change_theme_warning), Toast.LENGTH_LONG).show()
			callback!!.onThemeChanged()
			return true
		} else if (preference.key.equals("app_language", ignoreCase = true)) {
			if (newValue.toString().equals(appLanguage!!, ignoreCase = true))
				return true
			callback!!.onThemeChanged()
			return true
		}
		return false
	}

	override fun onDestroyView() {
		disposable.clear()
		super.onDestroyView()
	}

	override fun onDestroy() {
		disposable.clear()
		super.onDestroy()
	}

	private fun addCustomization() {
		addPreferencesFromResource(R.xml.customization_settings)
		findPreference("recylerViewAnimation").onPreferenceChangeListener = this
		findPreference("appColor").onPreferenceChangeListener = this
	}


	companion object {

		val TAG = SettingsCategoryFragment::class.java.simpleName
	}
}
