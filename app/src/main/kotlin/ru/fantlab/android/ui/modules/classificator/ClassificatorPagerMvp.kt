package ru.fantlab.android.ui.modules.classificator

import ru.fantlab.android.ui.base.mvp.BaseMvp

interface ClassificatorPagerMvp {

	interface View : BaseMvp.View {

		fun onSelected(extra: Int, add: Boolean)

		fun onSetBadge(tabIndex: Int, count: Int)

		fun onClassSended()

	}

	interface Presenter : BaseMvp.Presenter {

		fun onSendClassification(workId: Int, query: String)
	}
}