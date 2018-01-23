package ru.fantlab.android.ui.modules.main

import android.support.v4.app.FragmentManager
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface MainMvp {

	enum class NavigationType {
		NEWS,
		RESPONSES,
		FORUM
	}

	interface View : BaseMvp.View {

		fun onNavigationChanged(navType: NavigationType)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onModuleChanged(fragmentManager: FragmentManager, type: NavigationType)
	}
}