package ru.fantlab.android.ui.modules.main.forumfeed

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.ForumMessage
import ru.fantlab.android.data.dao.model.getForumMessages
import ru.fantlab.android.data.dao.model.save
import ru.fantlab.android.helper.observe
import ru.fantlab.android.provider.StubProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import timber.log.Timber
import java.util.*

class ForumFeedPresenter : BasePresenter<ForumFeedMvp.View>(), ForumFeedMvp.Presenter {

	private var messageModels: ArrayList<ForumMessage> = ArrayList()
	private var page: Int = 0
	private var previousTotal: Int = 0
	private var lastPage = Int.MAX_VALUE

	override fun onFragmentCreated() {
		if (messageModels.isEmpty()) {
			onCallApi(1)
		}
	}

	override fun onCallApi(page: Int): Boolean {
		if (page == 1) {
			lastPage = Int.MAX_VALUE
			sendToView { view -> view.getLoadMore().reset() }
		}
		if (page > lastPage || lastPage == 0) {
			sendToView { view -> view.hideProgress() }
			return false
		}
		setCurrentPage(page)
		makeRestCall(/*RestProvider.getCommonService()*/StubProvider.getForumFeed(page), Consumer { response ->
			run {
				lastPage = response.last
				if (getCurrentPage() == 1) {
					manageDisposable(response.items.save())
				}
				sendToView { view -> view.onNotifyAdapter(response.items, page) }
				sendToView { view -> view.showErrorMessage("API not ready yet") }
			}
		})
		return true
	}

	override fun getCurrentPage(): Int = page

	override fun getPreviousTotal(): Int = previousTotal

	override fun setCurrentPage(page: Int) {
		this.page = page
	}

	override fun setPreviousTotal(previousTotal: Int) {
		this.previousTotal = previousTotal
	}

	override fun onCallApi(page: Int, parameter: Any?): Boolean = onCallApi(page)

	override fun getMessages(): ArrayList<ForumMessage> = messageModels

	override fun onWorkOffline() {
		if (messageModels.isEmpty()) {
			manageDisposable(
					getForumMessages().toObservable().observe().subscribe(
							{ modelList ->
								modelList?.let {
									sendToView { view -> view.onNotifyAdapter(modelList, 1) }
								}
							},
							Timber::e
					)
			)
		} else {
			sendToView { view -> view.hideProgress() }
		}
	}

	override fun onItemClick(position: Int, v: View?, item: ForumMessage?) {
		// todo implement
	}

	override fun onItemLongClick(position: Int, v: View?, item: ForumMessage?) {
		// todo implement
	}
}