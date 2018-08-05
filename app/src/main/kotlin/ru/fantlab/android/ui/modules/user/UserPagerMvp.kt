package ru.fantlab.android.ui.modules.user

import ru.fantlab.android.ui.base.mvp.BaseMvp

/**
 * Created by Kosh on 08 Dec 2016, 8:19 PM
 */

interface UserPagerMvp {

	interface View : BaseMvp.View {

		fun onSetBadge(tabIndex: Int, count: Int)

		fun onSelectTab(tabIndex: Int)
	}

	interface Presenter : BaseMvp.Presenter
}