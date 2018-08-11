package ru.fantlab.android.ui.modules.work.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.model.Awards
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.data.dao.model.Work
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ListDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface WorkOverviewMvp {

	interface View : BaseMvp.View,
			android.view.View.OnClickListener, ListDialogView.ListDialogViewActionCallback {

		fun onInitViews(work: Work)

		fun onItemClicked(item: Nomination)
	}

	interface Presenter : BaseMvp.Presenter,
			android.view.View.OnClickListener,
			BaseViewHolder.OnItemClickListener<Nomination>{

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)

		fun getNoms(): ArrayList<Nomination>?

		fun getWins(): ArrayList<Nomination>?

	}
}