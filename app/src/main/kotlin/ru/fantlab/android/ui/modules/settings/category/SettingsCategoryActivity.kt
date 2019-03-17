package ru.fantlab.android.ui.modules.settings.category

import android.app.Activity
import android.os.Bundle
import com.evernote.android.state.State
import ru.fantlab.android.R
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.ui.base.BaseActivity

class SettingsCategoryActivity : BaseActivity<SettingsCategoryMvp.View, SettingsCategoryPresenter>(), SettingsCategoryFragment.SettingsCallback {
	var title: String? = null

	private var settingsType: Int = 0
	@State var needRecreation: Boolean = false

	override fun layout(): Int {
		return R.layout.activity_settings_category
	}

	override fun isTransparent()= false

	override fun canBack()= true

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setResult(Activity.RESULT_CANCELED)
		if (savedInstanceState == null) {
			val bundle = intent.extras
			title = bundle.getString(BundleConstant.EXTRA)
			settingsType = bundle.getInt(BundleConstant.ITEM)
			supportFragmentManager
					.beginTransaction()
					.replace(R.id.settingsContainer, SettingsCategoryFragment(), SettingsCategoryFragment.TAG)
					.commit()
		}
		setTitle(title)
	}

	override fun providePresenter() =  SettingsCategoryPresenter()


	override fun getSettingsType(): Int {
		return settingsType
	}

	override fun onThemeChanged() {
		needRecreation = true
	}

	override fun onBackPressed() {
		if (needRecreation) {
			super.onThemeChanged()
			return
		}
		super.onBackPressed()
	}
}
