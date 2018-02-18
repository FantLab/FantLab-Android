package ru.fantlab.android.ui.modules.profile.marks

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.UserMark
import ru.fantlab.android.data.dao.model.getUserMarks
import ru.fantlab.android.data.dao.model.save
import ru.fantlab.android.helper.observe
import ru.fantlab.android.provider.StubProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ProfileMarksPresenter : BasePresenter<ProfileMarksMvp.View>(), ProfileMarksMvp.Presenter {

	private var marks: ArrayList<UserMark> = ArrayList()
	private var page: Int = 0
	private var previousTotal: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE

	override fun onItemClick(position: Int, v: View?, item: UserMark) {
		view?.onItemClicked(item)
	}

	override fun onItemLongClick(position: Int, v: View?, item: UserMark?) {
	}

	override fun getMarks(): ArrayList<UserMark> = marks

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
			sendToView { view -> view.getLoadMore().reset() }
		}
		setCurrentPage(page)
		if (page > lastPage || lastPage == 0 || parameter == null) {
			sendToView { view -> view.hideProgress() }
			return false
		}
		makeRestCall(/*RestProvider.getUserService().getMarks(parameter, page)*/StubProvider.getMarks(parameter, page), Consumer { response ->
			lastPage = response.last
			manageDisposable(response.items.save(parameter))
			sendToView { view -> view.onNotifyAdapter(response.items, page) }
			sendToView { view -> view.showErrorMessage("API not ready yet") }
		})
		return true
	}

	override fun onError(throwable: Throwable) {
		sendToView { view ->
			view.getLoadMore().parameter?.let { onWorkOffline(it) }
		}
		super.onError(throwable)
	}

	override fun onWorkOffline(userId: Int) {
		if (marks.isEmpty()) {
			manageDisposable(
					getUserMarks(userId).toObservable()
							.observe()
							.subscribe({ sendToView { view -> view.onNotifyAdapter(it, 1) } })
			)
		} else {
			sendToView { view -> view.hideProgress() }
		}
	}
}