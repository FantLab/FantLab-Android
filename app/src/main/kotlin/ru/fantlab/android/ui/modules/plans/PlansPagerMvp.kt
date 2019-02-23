package ru.fantlab.android.ui.modules.plans

import ru.fantlab.android.ui.base.mvp.BaseMvp

interface PlansPagerMvp {

	interface View : BaseMvp.View {

		fun onScrolled(isUp: Boolean)

	}

	interface Presenter : BaseMvp.Presenter
}