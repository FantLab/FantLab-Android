package ru.fantlab.android.ui.modules.work

import ru.fantlab.android.ui.base.mvp.BaseMvp

interface WorkPagerMvp {

	interface View : BaseMvp.View {

		fun onSetBadge(tabIndex: Int, count: Int)

		fun onSetTitle(title: String)

		fun onScrolled(isUp: Boolean)

		fun onSetMarked(isMarked: Boolean, mark: Int)

		fun onGetMark(): Int?

		fun onResponsesRefresh()

		fun isCycle(): Boolean

		fun onError()
	}

	interface Presenter : BaseMvp.Presenter
}