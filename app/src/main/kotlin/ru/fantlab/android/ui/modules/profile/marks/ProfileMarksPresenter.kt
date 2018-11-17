package ru.fantlab.android.ui.modules.profile.marks

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Mark
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ProfileMarksPresenter : BasePresenter<ProfileMarksMvp.View>(), ProfileMarksMvp.Presenter {

	private var marks: ArrayList<Mark> = ArrayList()
	private var page: Int = 1
	private var previousTotal: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE

	override fun onItemClick(position: Int, v: View?, item: Mark) {
		view?.onItemClicked(item)
	}

	override fun onItemLongClick(position: Int, v: View, item: Mark) {
		view?.onItemLongClicked(position, v, item)
	}

	override fun getMarks(): ArrayList<Mark> = marks

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
		makeRestCall(DataManager.getUserMarks(parameter, page)
				.toObservable(),
				Consumer {
					lastPage = it.marks.last
					sendToView { view ->
						view.onNotifyAdapter(it.marks.items, page)
						view.onSetTabCount(it.marks.totalCount)
					}
				})
		return true
	}

	fun onSendMark(item: Mark, mark: Int, position: Int) {
		makeRestCall(DataManager.sendUserMark(item.workId, item.workId, mark)
				.toObservable(),
				Consumer {
					sendToView { view ->
						view.onSetMark(position, mark)
					}
				})
	}

	override fun onError(throwable: Throwable) {
		sendToView {
			it.getLoadMore().parameter?.let { onWorkOffline(it) }
		}
		super.onError(throwable)
	}

	override fun onWorkOffline(userId: Int) {
		sendToView { it.hideProgress() }
		sendToView { it.showMessage(R.string.error, R.string.failed_data) }
	}
}