package ru.fantlab.android.ui.modules.award.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface AwardOverviewMvp {

	interface View : BaseMvp.View {

		fun onInitViews(award: Award)

		fun onSetTitle(title: String)

		fun onShowErrorView(msgRes: String?)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle)
	}
}