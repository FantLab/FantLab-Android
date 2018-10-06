package ru.fantlab.android.ui.modules.author

import ru.fantlab.android.ui.base.mvp.BaseMvp

interface AuthorPagerMvp {

	interface View : BaseMvp.View {

		fun onSetBadge(tabIndex: Int, count: Int)

		fun onSetTitle(title: String)
	}

	interface Presenter : BaseMvp.Presenter
}