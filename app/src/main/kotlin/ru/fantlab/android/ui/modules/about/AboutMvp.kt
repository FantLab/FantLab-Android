package ru.fantlab.android.ui.modules.about

import ru.fantlab.android.ui.base.mvp.BaseMvp

interface AboutMvp {

	interface View : BaseMvp.View, android.view.View.OnClickListener

	interface Presenter : BaseMvp.Presenter
}