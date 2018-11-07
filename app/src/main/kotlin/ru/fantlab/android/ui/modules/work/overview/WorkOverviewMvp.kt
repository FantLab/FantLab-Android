package ru.fantlab.android.ui.modules.work.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ListDialogView
import ru.fantlab.android.ui.widgets.dialog.RatingDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface WorkOverviewMvp {

	interface View : BaseMvp.View,
			android.view.View.OnClickListener,
			ListDialogView.ListDialogViewActionCallback,
			RatingDialogView.RatingDialogViewActionCallback {

		fun onInitViews(work: Work)

		fun onItemClicked(item: Nomination)

		fun onSetMark(mark: Int, markCount: Double, midMark: Double)

		fun onGetMarks(marks: ArrayList<MarkMini>)

		fun onSetTitle(title: String)
	}

	interface Presenter : BaseMvp.Presenter,
			android.view.View.OnClickListener,
			BaseViewHolder.OnItemClickListener<Nomination>{

		fun onFragmentCreated(bundle: Bundle?)

		fun onWorkOffline(id: Int)

		fun getNoms(): ArrayList<Nomination>?

		fun getWins(): ArrayList<Nomination>?

		fun getAuthors(): ArrayList<Work.Author>?

		fun getMarks(userId: Int?, workIds: ArrayList<Int?>)

	}
}