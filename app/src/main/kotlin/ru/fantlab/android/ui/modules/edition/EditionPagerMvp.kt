package ru.fantlab.android.ui.modules.edition

import ru.fantlab.android.ui.base.mvp.BaseMvp

interface EditionPagerMvp {

	interface View : BaseMvp.View {

		fun onSetBadge(tabIndex: Int, count: Int)
	}

	interface Presenter : BaseMvp.Presenter
}