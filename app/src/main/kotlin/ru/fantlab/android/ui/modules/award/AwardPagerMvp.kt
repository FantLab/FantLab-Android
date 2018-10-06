package ru.fantlab.android.ui.modules.award

import ru.fantlab.android.ui.base.mvp.BaseMvp

interface AwardPagerMvp {

	interface View : BaseMvp.View {

		fun onSetBadge(tabIndex: Int, count: Int)

		fun onSetTitle(title: String)
	}

	interface Presenter : BaseMvp.Presenter
}