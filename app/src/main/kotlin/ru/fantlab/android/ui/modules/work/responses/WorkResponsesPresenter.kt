package ru.fantlab.android.ui.modules.work.responses

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Response
import ru.fantlab.android.data.dao.response.VoteResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.ResponsesSortOption
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkResponsesPresenter : BasePresenter<WorkResponsesMvp.View>(),
		WorkResponsesMvp.Presenter {

	private var responses: ArrayList<Response> = ArrayList()
	private var page: Int = 1
	private var sort: ResponsesSortOption? = null
	private var previousTotal: Int = 0
	private var workId: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE

	override fun onItemClick(position: Int, v: View?, item: Response) {
		view?.onItemClicked(item)
	}

	override fun onItemLongClick(position: Int, v: View?, item: Response) {
		view?.onItemLongClicked(position, v, item)
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

	fun setCurrentSort(sortValue: String){
		sort = ResponsesSortOption.valueOf(sortValue)
		onCallApi(1, workId)
	}

	override fun onCallApi(page: Int, parameter: Int?): Boolean {
		workId = parameter!!
		if (page == 1) {
			lastPage = Integer.MAX_VALUE
			sendToView { it.getLoadMore().reset() }
		}
		setCurrentPage(page)
		if (page > lastPage || lastPage == 0 || parameter == null) {
			sendToView { it.hideProgress() }
			return false
		}
		makeRestCall(
				DataManager.getWorkResponses(parameter, page, sort ?: ResponsesSortOption.BY_RATING)
						.map { it.get() }
						.toObservable(),
				Consumer {
			lastPage = it.responses.last
			//manageDisposable(it.items.save())
			sendToView { view ->
				view.onNotifyAdapter(it.responses.items, page)
				view.onSetTabCount(it.responses.totalCount)
			}
		}
		)
		return true
	}

	override fun onError(throwable: Throwable) {
		sendToView { it.getLoadMore().parameter?.let { onWorkOffline(it) } }
		super.onError(throwable)
	}

	override fun onWorkOffline(workId: Int) {
		sendToView { it.showErrorMessage("Не удалось загрузить данные") }
	}

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