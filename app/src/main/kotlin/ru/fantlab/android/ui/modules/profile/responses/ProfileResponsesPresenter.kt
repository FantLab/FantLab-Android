package ru.fantlab.android.ui.modules.profile.responses

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.response.ResponsesResponse
import ru.fantlab.android.data.dao.response.VoteResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getUserResponsesPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ProfileResponsesPresenter : BasePresenter<ProfileResponsesMvp.View>(),
		ProfileResponsesMvp.Presenter {

	private var page: Int = 1
	private var previousTotal: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE

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
		getResponses(parameter, false)
		return true
	}

	override fun getResponses(userId: Int, force: Boolean) {
		if (force) {
			lastPage = Integer.MAX_VALUE
			sendToView { it.getLoadMore().reset() }
			setCurrentPage(1)
		}
		makeRestCall(
				getResponsesInternal(userId, force).toObservable(),
				Consumer { (responses, totalCount, lastPage) ->
					this.lastPage = lastPage
					sendToView {
						with (it) {
							onNotifyAdapter(responses, page)
							onSetTabCount(totalCount)
						}
					}
				}
		)
	}

	private fun getResponsesInternal(userId: Int, force: Boolean) =
			getResponsesFromServer(userId)
					.onErrorResumeNext { throwable ->
						if (page == 1 && !force) {
							getResponsesFromDb(userId)
						} else {
							throw throwable
						}
					}

	private fun getResponsesFromServer(userId: Int): Single<Triple<ArrayList<Response>, Int, Int>> =
			DataManager.getUserResponses(userId, page)
					.map { getResponses(it) }

	private fun getResponsesFromDb(userId: Int): Single<Triple<ArrayList<Response>, Int, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getUserResponsesPath(userId, 1))
					.map { it.response }
					.map { ResponsesResponse.Deserializer(perPage = 50).deserialize(it) }
					.map { getResponses(it) }

	private fun getResponses(response: ResponsesResponse): Triple<ArrayList<Response>, Int, Int> =
			Triple(response.responses.items, response.responses.totalCount, response.responses.last)

	override fun onSendVote(item: Response, position: Int, voteType: String) {
		makeRestCall(
				DataManager.sendResponseVote(item.id, voteType).toObservable(),
				Consumer { response ->
					val result = VoteResponse.Parser().parse(response)
					if (result != null) {
						sendToView { it.onSetVote(position, result.votesCount.toString()) }
					} else {
						sendToView { it.showErrorMessage(response) }
					}
				})
	}

	override fun onItemClick(position: Int, v: View?, item: Response) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Response?) {
	}

	override fun getCurrentPage(): Int = page

	override fun getPreviousTotal(): Int = previousTotal

	override fun setCurrentPage(page: Int) {
		this.page = page
	}

	override fun setPreviousTotal(previousTotal: Int) {
		this.previousTotal = previousTotal
	}
}