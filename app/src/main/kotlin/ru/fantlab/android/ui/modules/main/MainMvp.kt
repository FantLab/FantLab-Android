package ru.fantlab.android.ui.modules.main

import androidx.fragment.app.FragmentManager
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface MainMvp {

	enum class NavigationType {
		NEWS,
		NEWFICTION,
		RESPONSES
	}

	interface View : BaseMvp.View {

		fun onNavigationChanged(navType: NavigationType)
	}

	interface Presenter : BaseMvp.Presenter, BottomNavigation.OnMenuItemSelectionListener {

		fun onModuleChanged(fragmentManager: FragmentManager, type: NavigationType)
	}
}