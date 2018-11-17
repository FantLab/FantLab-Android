package ru.fantlab.android.ui.modules.work.responses.overview

import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.ui.base.mvp.BaseMvp
import ru.fantlab.android.ui.widgets.dialog.ContextMenuDialogView

interface ResponseOverviewMvp {

	interface View : BaseMvp.View,
			ContextMenuDialogView.ListDialogViewActionCallback {

		fun onInitViews(response: Response)

		fun onSetVote(votesCount: String)
	}

	interface Presenter : BaseMvp.Presenter {

		fun initResponse(response: Response?)

		fun getResponses(): Response
	}
}