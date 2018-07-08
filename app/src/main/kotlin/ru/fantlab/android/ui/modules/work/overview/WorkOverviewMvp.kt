package ru.fantlab.android.ui.modules.work.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.model.Work
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ListDialogView

interface WorkOverviewMvp {

	interface View : BaseMvp.View,
			android.view.View.OnClickListener, ListDialogView.ListDialogViewActionCallback {

		fun onInitViews(work: Work)
	}

	interface Presenter : BaseMvp.Presenter,
			android.view.View.OnClickListener{

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)

	}
}