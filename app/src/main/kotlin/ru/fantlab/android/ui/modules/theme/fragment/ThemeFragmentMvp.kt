package ru.fantlab.android.ui.modules.theme.fragment

import androidx.annotation.ColorInt
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface ThemeFragmentMvp {

	interface ThemeListener {
		fun onChangePrimaryDarkColor(@ColorInt color: Int, darkIcons: Boolean)

		fun onThemeApplied()
	}

	interface View : BaseMvp.View

	interface Presenter
}
