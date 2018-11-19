package ru.fantlab.android.ui.modules.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import butterknife.BindView
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.SettingsModel
import ru.fantlab.android.ui.adapter.SettingsAdapter
import ru.fantlab.android.ui.base.BaseActivity
import ru.fantlab.android.ui.modules.theme.ThemeActivity
import ru.fantlab.android.ui.widgets.dialog.LanguageBottomSheetDialog
import ru.fantlab.android.ui.widgets.recyclerview.DynamicRecyclerView
import kotlin.reflect.KFunction0

class SettingsActivity : BaseActivity<SettingsMvp.View, SettingsPresenter>(), SettingsMvp.View,
		LanguageBottomSheetDialog.LanguageDialogViewActionCallback {

	override fun isTransparent(): Boolean = false
	override fun providePresenter(): SettingsPresenter = SettingsPresenter()
	override fun layout(): Int = R.layout.activity_settings
	override fun canBack(): Boolean = true

	private val adapter: SettingsAdapter by lazy { SettingsAdapter(arrayListOf()) }

	@BindView(R.id.recycler) lateinit var recycler: DynamicRecyclerView
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
		adapter.addItem(SettingsModel(R.drawable.ic_color, getString(R.string.theme_title), "", SettingsModel.THEME))
		adapter.addItem(SettingsModel(R.drawable.ic_language, getString(R.string.language), "", SettingsModel.LANGUAGE))
	}

	override fun onItemClicked(item: SettingsModel) {
		when (item.settingsType) {
			SettingsModel.THEME -> {
				startActivityForResult(Intent(this, ThemeActivity::class.java), THEME_CHANGE)
			}
			SettingsModel.LANGUAGE -> {
				showLanguageList()
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