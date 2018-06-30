package ru.fantlab.android.ui.modules.work.responses.overview

import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.ui.base.mvp.BaseMvp

interface ResponseOverviewMvp {

	interface View : BaseMvp.View {

		fun onInitViews(response: Response)
	}

	interface Presenter : BaseMvp.Presenter {

		fun initResponse(response: Response?)

		fun getResponses(): Response
	}
}