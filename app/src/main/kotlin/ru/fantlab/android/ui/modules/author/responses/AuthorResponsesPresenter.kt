package ru.fantlab.android.ui.modules.author.responses

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.response.ResponsesResponse
import ru.fantlab.android.data.dao.response.VoteResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.ResponsesSortOption
import ru.fantlab.android.provider.rest.getAuthorResponsesPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AuthorResponsesPresenter : BasePresenter<AuthorResponsesMvp.View>(),
		AuthorResponsesMvp.Presenter {

	var responses: ArrayList<Response> = ArrayList()
		private set
	private var page: Int = 1
	private var sort = ResponsesSortOption.BY_DATE
	private var previousTotal: Int = 0
	private var authorId: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE

	override fun onCallApi(page: Int, parameter: Int?): Boolean {
		authorId = parameter!!
		if (page == 1) {
			lastPage = Integer.MAX_VALUE
			sendToView { it.getLoadMore().reset() }
		}
		setCurrentPage(page)
		if (page > lastPage || lastPage == 0) {
			sendToView { it.hideProgress() }
			return false
		}
		makeRestCall(
				getResponsesInternal(page).toObservable(),
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
		return true
	}

	private fun getResponsesInternal(page: Int) =
			getResponsesFromServer(page)
					.onErrorResumeNext { throwable ->
						if (page == 1) {
							getResponsesFromDb()
						} else {
							throw throwable
						}
					}

	private fun getResponsesFromServer(page: Int): Single<Triple<ArrayList<Response>, Int, Int>> =
			DataManager.getAuthorResponses(authorId, page, sort)
					.map { getResponses(it) }

	private fun getResponsesFromDb(): Single<Triple<ArrayList<Response>, Int, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getAuthorResponsesPath(authorId, 1, sort))
					.map { it.response }
					.map { ResponsesResponse.Deserializer(perPage = 50).deserialize(it) }
					.map { getResponses(it) }

	private fun getResponses(response: ResponsesResponse): Triple<ArrayList<Response>, Int, Int> =
			Triple(response.responses.items, response.responses.totalCount, response.responses.last)

	override fun setCurrentSort(sortValue: String) {
		sort = ResponsesSortOption.valueOf(sortValue)
		onCallApi(1, authorId)
	}

	override fun getCurrentSort() = sort

	override fun onSendVote(responseId: Int, voteType: String, position: Int) {
		makeRestCall(
				voteInternal(responseId, voteType).toObservable(),
				Consumer { votesCount -> sendToView { it.onSetVote(votesCount, position) } }
		)
	}

	private fun voteInternal(responseId: Int, voteType: String) =
			DataManager.sendResponseVote(responseId, voteType)
					.map { VoteResponse.Parser().parse(it)!!.votesCount }

	override fun onItemClick(position: Int, v: View?, item: Response) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Response) {
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