package ru.fantlab.android.ui.modules.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.SettingsModel
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.Bundler
import ru.fantlab.android.ui.adapter.SettingsAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.settings.category.SettingsCategoryActivity
import ru.fantlab.android.ui.modules.theme.ThemeActivity
import ru.fantlab.android.ui.widgets.dialog.FontScaleBottomSheetDialog
import ru.fantlab.android.ui.widgets.dialog.LanguageBottomSheetDialog
import kotlin.reflect.KFunction0

class SettingsActivity : BaseActivity<SettingsMvp.View, SettingsPresenter>(), SettingsMvp.View,
		LanguageBottomSheetDialog.LanguageDialogViewActionCallback{

	override fun isTransparent(): Boolean = false
	override fun providePresenter(): SettingsPresenter = SettingsPresenter()
	override fun layout(): Int = R.layout.activity_settings
	override fun canBack(): Boolean = true

	private val adapter: SettingsAdapter by lazy { SettingsAdapter(arrayListOf()) }

	private val THEME_CHANGE = 55

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		title = getString(R.string.settings)
		if (savedInstanceState == null) {
			setResult(RESULT_CANCELED)
		}
		adapter.listener = presenter
		recycler.addDivider()
		recycler.adapter = adapter
		adapter.addItem(SettingsModel(R.drawable.ic_language, getString(R.string.language), "", SettingsModel.LANGUAGE))
		adapter.addItem(SettingsModel(R.drawable.ic_theme, getString(R.string.theme_title), "", SettingsModel.THEME))
		//adapter.addItem(SettingsModel(R.drawable.ic_forum, getString(R.string.forum), "", SettingsModel.FORUM))
		adapter.addItem(SettingsModel(R.drawable.ic_custom, getString(R.string.customization), getString(R.string.customizationHint), SettingsModel.CUSTOMIZATION))
		hideShowShadow(true)
	}

	override fun onItemClicked(item: SettingsModel) {
		when (item.settingsType) {
			SettingsModel.THEME -> {
				startActivityForResult(Intent(this, ThemeActivity::class.java), THEME_CHANGE)
			}
			SettingsModel.LANGUAGE -> {
				showLanguageList()
			}
			else -> {
				val intent = Intent(this, SettingsCategoryActivity::class.java)
				intent.putExtras(Bundler.start()
						.put(BundleConstant.ITEM, item.settingsType)
						.put(BundleConstant.EXTRA, item.title)
						.end())
				startActivity(intent)
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (requestCode == THEME_CHANGE && resultCode == Activity.RESULT_OK) {
			setResult(resultCode)
			finish()
		}
	}

	private fun showLanguageList() {
		val languageBottomSheetDialog = LanguageBottomSheetDialog()
		languageBottomSheetDialog.onAttach(this as Context)
		languageBottomSheetDialog.show(supportFragmentManager, "LanguageBottomSheetDialog")
	}

	override fun onLanguageChanged(action: KFunction0<Unit>) {
		action.run { }
		setResult(Activity.RESULT_OK)
		finish()
	}
}