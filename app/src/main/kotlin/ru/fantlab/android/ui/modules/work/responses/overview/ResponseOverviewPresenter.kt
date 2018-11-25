package ru.fantlab.android.ui.modules.work.responses.overview

import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.response.VoteResponse
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ResponseOverviewPresenter : BasePresenter<ResponseOverviewMvp.View>(),
		ResponseOverviewMvp.Presenter {

	override fun onSendVote(item: Response, voteType: String) {
		makeRestCall(
				DataManager.sendResponseVote(item.id, voteType).toObservable(),
				Consumer { voteResponse ->
					val result = VoteResponse.Parser().parse(voteResponse)
					if (result != null) {
						sendToView { it.onSetVote(result.votesCount.toString()) }
					} else {
						sendToView { it.showErrorMessage(voteResponse) }
					}
				})
	}

	override fun onGetUserLevel() {
		makeRestCall(
				DataManager.getUser(PrefGetter.getLoggedUser()?.id!!).toObservable(),
				Consumer { userResponse ->
					sendToView { it.onShowVotesDialog(userResponse.user.level) }
				}
		)
	}
}