package ru.fantlab.android.ui.modules.profile.responses

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.response.VoteResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ProfileResponsesPresenter : BasePresenter<ProfileResponsesMvp.View>(),
		ProfileResponsesMvp.Presenter {

	private var responses: ArrayList<Response> = ArrayList()
	private var page: Int = 1
	private var previousTotal: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE

	override fun onItemClick(position: Int, v: View?, item: Response) {
		view?.onItemClicked(item)
	}

	override fun onItemLongClick(position: Int, v: View?, item: Response?) {
	}

	override fun getResponses(): ArrayList<Response> = responses

	override fun getCurrentPage(): Int = page

	override fun getPreviousTotal(): Int = previousTotal

	override fun setCurrentPage(page: Int) {
		this.page = page
	}

	override fun setPreviousTotal(previousTotal: Int) {
		this.previousTotal = previousTotal
	}

	override fun onCallApi(page: Int, parameter: Int?): Boolean {
		if (page == 1) {
			lastPage = Integer.MAX_VALUE
			sendToView { it.getLoadMore().reset() }
		}
		setCurrentPage(page)
		if (page > lastPage || lastPage == 0 || parameter == null) {
			sendToView { it.hideProgress() }
			return false
		}
		makeRestCall(DataManager.getUserResponses(parameter, page)
				.toObservable(),
				Consumer {
					lastPage = it.responses.last
					sendToView { view ->
						view.onNotifyAdapter(it.responses.items, page)
						view.onSetTabCount(it.responses.totalCount)
					}
				})
		return true
	}

	override fun onError(throwable: Throwable) {
		sendToView { it.getLoadMore().parameter?.let { onWorkOffline(it) } }
		super.onError(throwable)
	}

	override fun onWorkOffline(userId: Int) {
		sendToView { it.hideProgress() }
		sendToView { it.showMessage(R.string.error, R.string.failed_data) }
	}

	fun onSendVote(item: Response, position: Int, voteType: String) {
		makeRestCall(DataManager.sendResponseVote(item.id, voteType)
				.toObservable(),
				Consumer { response ->
					val result = VoteResponse.Parser().parse(response)
					if (result != null) {
						sendToView { view ->
							view.onSetVote(position, result.votesCount.toString())
						}
					} else {
						sendToView { it.showErrorMessage(response) }
					}
				})
	}
}