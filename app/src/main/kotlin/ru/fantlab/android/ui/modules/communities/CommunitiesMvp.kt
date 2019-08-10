package ru.fantlab.android.ui.modules.communities

import ru.fantlab.android.ui.base.mvp.BaseMvp

interface CommunitiesMvp {

	interface View : BaseMvp.View  {

		fun setTitle(title: String)
	}

	interface Presenter : BaseMvp.Presenter {
	}
}