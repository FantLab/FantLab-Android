package ru.fantlab.android.ui.modules.work.overview

import android.os.Bundle
import ru.fantlab.android.data.dao.model.*
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
				rootSagas: ArrayList<WorkRootSaga>,
				awards: ArrayList<Nomination>,
				authors: ArrayList<Work.Author>
		)

		fun onSetClassification(classificatory: ArrayList<GenreGroup>)

		fun onSetEditions(editions: ArrayList<EditionsBlocks.EditionsBlock>)

		fun onItemClicked(item: Nomination)

		fun onSetMark(mark: Int, markCount: String, midMark: String)

		fun onGetMarks(marks: ArrayList<MarkMini>)

		fun onSetTitle(title: String)

		fun onShowErrorView(msgRes: String?)
	}

	interface Presenter : BaseMvp.Presenter,
			BaseViewHolder.OnItemClickListener<Nomination> {

		fun onFragmentCreated(bundle: Bundle)

		fun getMarks(userId: Int, workIds: ArrayList<Int>)

		fun getClassification()

		fun onSendMark(workId: Int, mark: Int)
	}
}