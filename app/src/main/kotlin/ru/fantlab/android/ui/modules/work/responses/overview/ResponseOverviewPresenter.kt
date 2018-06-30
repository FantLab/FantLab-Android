package ru.fantlab.android.ui.modules.work.responses.overview

import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ResponseOverviewPresenter : BasePresenter<ResponseOverviewMvp.View>(),
		ResponseOverviewMvp.Presenter {

	var response: Response? = null

	override fun initResponse(response: Response?) {
		if (response == null) {
			throw NullPointerException("Either bundle or Response is null")
		}
        sendToView { it.onInitViews(response) }
	}

	override fun getResponses(): Response = response!!
}