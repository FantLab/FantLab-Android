package ru.fantlab.android.ui.modules.blogs

import ru.fantlab.android.ui.base.mvp.BaseMvp

interface BlogsMvp {

	interface View : BaseMvp.View  {

		fun setTitle(title: String)

	}

	interface Presenter : BaseMvp.Presenter {

	}
}