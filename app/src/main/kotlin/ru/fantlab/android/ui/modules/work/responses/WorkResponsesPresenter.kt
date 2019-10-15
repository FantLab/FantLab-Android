package ru.fantlab.android.ui.modules.work.responses

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.response.ResponsesResponse
import ru.fantlab.android.data.dao.response.VoteResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.ResponsesSortOption
import ru.fantlab.android.provider.rest.getWorkResponsesPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkResponsesPresenter : BasePresenter<WorkResponsesMvp.View>(),
		WorkResponsesMvp.Presenter {

	private var page: Int = 1
	private var sort: ResponsesSortOption = ResponsesSortOption.BY_DATE
	private var previousTotal: Int = 0
	private var workId: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE

	override fun onCallApi(page: Int, parameter: Int?): Boolean {
		workId = parameter!!
		if (page == 1) {
			lastPage = Integer.MAX_VALUE
			sendToView { it.getLoadMore().reset() }
		}
		setCurrentPage(page)
		if (page > lastPage || lastPage == 0) {
			sendToView { it.hideProgress() }
			return false
		}
		getResponses(page, false)
		return true
	}

	override fun getResponses(page: Int, force: Boolean) {
		makeRestCall(
				getResponsesInternal(page, force).toObservable(),
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

	private fun getResponsesInternal(page: Int, force: Boolean) =
			getResponsesFromServer(page)
					.onErrorResumeNext { throwable ->
						if (page == 1 && !force) {
							getResponsesFromDb(page)
						} else {
							throw throwable
						}
					}

	private fun getResponsesFromServer(page: Int): Single<Triple<ArrayList<Response>, Int, Int>> =
			DataManager.getWorkResponses(workId, page, sort)
					.map { getResponses(it) }

	private fun getResponsesFromDb(page: Int): Single<Triple<ArrayList<Response>, Int, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getWorkResponsesPath(workId, page, sort))
					.map { it.response }
					.map { ResponsesResponse.Deserializer(perPage = 15).deserialize(it) }
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
				}
		)
	}

	override fun onDeleteResponse(workId: Int, commentId: Int, position: Int) {
		makeRestCall(
				DataManager.editResponse(workId, commentId, "").toObservable(),
				Consumer { sendToView { it.onResponseDelete(position) } }
		)
	}

	override fun setCurrentSort(sortValue: String) {
		sort = ResponsesSortOption.valueOf(sortValue)
		onCallApi(1, workId)
	}

	override fun getCurrentSort() = sort

	override fun onItemClick(position: Int, v: View?, item: Response) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Response) {
		sendToView { it.onItemLongClicked(position, v, item) }
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