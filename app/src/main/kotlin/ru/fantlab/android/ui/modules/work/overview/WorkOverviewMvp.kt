package ru.fantlab.android.ui.modules.work.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.newmodel.Work
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface WorkOverviewMvp {

	interface View : BaseMvp.View {

		fun onInitViews(work: Work)
	}

	interface Presenter : BaseMvp.Presenter {

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)
	}
}