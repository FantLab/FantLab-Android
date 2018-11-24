package ru.fantlab.android.ui.modules.work.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.model.MarkMini
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.data.dao.model.Work
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ListDialogView
import ru.fantlab.android.ui.widgets.dialog.RatingDialogView
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

interface WorkOverviewMvp {

	interface View : BaseMvp.View,
			ListDialogView.ListDialogViewActionCallback,
			RatingDialogView.RatingDialogViewActionCallback {

		fun onInitViews(
				work: Work,
				nominations: ArrayList<Nomination>,
				wins: ArrayList<Nomination>,
				authors: ArrayList<Work.Author>
		)

		fun onItemClicked(item: Nomination)

		fun onSetMark(mark: Int, markCount: Double, midMark: Double)

		fun onGetMarks(marks: ArrayList<MarkMini>)

		fun onSetTitle(title: String)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Nomination> {

		fun onFragmentCreated(bundle: Bundle)

		fun getMarks(userId: Int, workIds: ArrayList<Int>)

		fun onSendMark(workId: Int, mark: Int)
	}
}