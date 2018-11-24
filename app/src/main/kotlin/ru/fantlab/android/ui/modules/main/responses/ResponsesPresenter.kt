package ru.fantlab.android.ui.modules.main.responses

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.response.ResponsesResponse
import ru.fantlab.android.data.dao.response.UserResponse
import ru.fantlab.android.data.dao.response.VoteResponse
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getLastResponsesPath
import ru.fantlab.android.provider.rest.getUserPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ResponsesPresenter : BasePresenter<ResponsesMvp.View>(), ResponsesMvp.Presenter {

	private var page: Int = 1
	private var previousTotal: Int = 0
	private var lastPage = Int.MAX_VALUE

	override fun onFragmentCreated() {
		onCallApi(1)
	}

	override fun onCallApi(page: Int, parameter: String?): Boolean = onCallApi(page)

	override fun onCallApi(page: Int): Boolean {
		if (page == 1) {
			lastPage = Int.MAX_VALUE
			sendToView { it.getLoadMore().reset() }
		}
		if (page > lastPage || lastPage == 0) {
			sendToView { it.hideProgress() }
			return false
		}
		setCurrentPage(page)
		makeRestCall(
				getResponsesInternal().toObservable(),
				Consumer { (responses, lastPage) ->
					this.lastPage = lastPage
					sendToView { it.onNotifyAdapter(responses, page) }
				}
		)
		return true
	}

	private fun getResponsesInternal() =
			getResponsesFromServer()
					.onErrorResumeNext { throwable ->
						if (page == 1) {
							getResponsesFromDb()
						} else {
							throw throwable
						}
					}

	private fun getResponsesFromServer(): Single<Pair<ArrayList<Response>, Int>> =
			DataManager.getLastResponses(page)
					.map { getResponses(it) }

	private fun getResponsesFromDb(): Single<Pair<ArrayList<Response>, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getLastResponsesPath(page))
					.map { it.response }
					.map { ResponsesResponse.Deserializer(perPage = 50).deserialize(it) }
					.map { getResponses(it) }

	private fun getResponses(response: ResponsesResponse): Pair<ArrayList<Response>, Int> =
			response.responses.items to response.responses.last

	override fun onSendVote(item: Response, position: Int, voteType: String) {
		makeRestCall(
				getUserLevelInternal().toObservable(),
				Consumer { level ->
					if (level >= FantlabHelper.minLevelToVote) {
						makeRestCall(DataManager.sendResponseVote(item.id, voteType)
								.toObservable(),
								Consumer { response ->
									val result = VoteResponse.Parser().parse(response)
									if (result != null) {
										sendToView { it.onSetVote(position, result.votesCount.toString()) }
									} else {
										sendToView { it.showErrorMessage(response) }
									}
								})
					} else {
						sendToView { it.showMessage(R.string.error, R.string.cannotvote_novice) }
					}
				}
		)
	}

	private fun getUserLevelInternal() =
			getUserLevelFromServer()
					.onErrorResumeNext {
						getUserLevelFromDb()
					}

	private fun getUserLevelFromServer(): Single<Float> =
			DataManager.getUser(PrefGetter.getLoggedUser()?.id!!)
					.map { getUserLevel(it) }

	private fun getUserLevelFromDb(): Single<Float> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getUserPath(PrefGetter.getLoggedUser()?.id!!))
					.map { it.response }
					.map { UserResponse.Deserializer().deserialize(it) }
					.map { getUserLevel(it) }

	private fun getUserLevel(response: UserResponse): Float = response.user.level

	override fun getCurrentPage(): Int = page

	override fun getPreviousTotal(): Int = previousTotal

	override fun setCurrentPage(page: Int) {
		this.page = page
	}

	override fun setPreviousTotal(previousTotal: Int) {
		this.previousTotal = previousTotal
	}

	override fun onItemClick(position: Int, v: View?, item: Response) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View, item: Response) {
		sendToView { it.onItemLongClicked(position, v, item) }
	}
}