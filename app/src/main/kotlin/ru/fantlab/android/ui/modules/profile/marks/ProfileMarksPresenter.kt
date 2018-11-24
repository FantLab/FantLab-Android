package ru.fantlab.android.ui.modules.profile.marks

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Mark
import ru.fantlab.android.data.dao.response.MarksResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getUserMarksPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ProfileMarksPresenter : BasePresenter<ProfileMarksMvp.View>(), ProfileMarksMvp.Presenter {

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
		getMarks(parameter, false)
		return true
	}

	override fun getMarks(userId: Int, force: Boolean) {
		if (force) {
			lastPage = Integer.MAX_VALUE
			sendToView { it.getLoadMore().reset() }
			setCurrentPage(1)
		}
		makeRestCall(
				getMarksInternal(userId, force).toObservable(),
				Consumer { (marks, totalCount, lastPage) ->
					this.lastPage = lastPage
					sendToView {
						with (it) {
							onNotifyAdapter(marks, page)
							onSetTabCount(totalCount)
						}
					}
				}
		)
	}

	private fun getMarksInternal(userId: Int, force: Boolean) =
			getMarksFromServer(userId)
					.onErrorResumeNext { throwable ->
						if (page == 1 && !force) {
							getMarksFromDb(userId)
						} else {
							throw throwable
						}
					}

	private fun getMarksFromServer(userId: Int): Single<Triple<ArrayList<Mark>, Int, Int>> =
			DataManager.getUserMarks(userId, page)
					.map { getMarks(it) }

	private fun getMarksFromDb(userId: Int): Single<Triple<ArrayList<Mark>, Int, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getUserMarksPath(userId, 1))
					.map { it.response }
					.map { MarksResponse.Deserializer(perPage = 200).deserialize(it) }
					.map { getMarks(it) }

	private fun getMarks(response: MarksResponse): Triple<ArrayList<Mark>, Int, Int> =
			Triple(response.marks.items, response.marks.totalCount, response.marks.last)

	override fun onSendMark(item: Mark, mark: Int, position: Int) {
		makeRestCall(
				DataManager.sendUserMark(item.workId, item.workId, mark).toObservable(),
				Consumer { _ -> sendToView { it.onSetMark(position, mark) } }
		)
	}

	override fun onItemClick(position: Int, v: View?, item: Mark) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View, item: Mark) {
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