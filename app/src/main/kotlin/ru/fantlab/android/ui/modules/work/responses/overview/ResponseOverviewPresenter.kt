package ru.fantlab.android.ui.modules.work.responses.overview

import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.response.VoteResponse
import ru.fantlab.android.provider.rest.DataManager
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

	fun onSendVote(item: Response, position: Int, voteType: String){
		makeRestCall(DataManager.sendResponseVote(item.id, voteType)
				.map { it.get() }
				.toObservable(),
				Consumer { response ->
					val result = VoteResponse.Parser().parse(response)
					if (result != null) {
						sendToView { view ->
							view.onSetVote(position, result.votesCount)
						}
					} else {
						sendToView { it.showErrorMessage(response) }
					}
				})
	}
}