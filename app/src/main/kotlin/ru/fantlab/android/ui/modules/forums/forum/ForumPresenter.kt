package ru.fantlab.android.ui.modules.forums.forum

import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Forum
import ru.fantlab.android.data.dao.response.ForumResponse
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getTopicsPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class ForumPresenter : BasePresenter<ForumMvp.View>(),
		ForumMvp.Presenter {

	private var page: Int = 1
	private var forumId: Int = 0
	private var lastPage: Int = Integer.MAX_VALUE
	private var previousTotal: Int = 0

	override fun onCallApi(page: Int, parameter: Int?): Boolean {
		forumId = parameter!!
		if (page == 1) {
			lastPage = Integer.MAX_VALUE
			sendToView { it.getLoadMore().reset() }
		}
		setCurrentPage(page)
		if (page > lastPage || lastPage == 0) {
			sendToView { it.hideProgress() }
			return false
		}
		getTopics(false)
		return true
	}

	override fun getTopics(force: Boolean) {
		makeRestCall(
				getTopicsInternal(force).toObservable(),
				Consumer { (forum, lastPage) -> sendToView {
					this.lastPage = lastPage
					it.onNotifyAdapter(forum, page)
				} }
		)
	}

	private fun getTopicsInternal(force: Boolean) =
			getTopicsFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getTopicsFromDb()
						} else {
							throw throwable
						}
					}

	private fun getTopicsFromServer(): Single<Pair<ArrayList<Forum.Topic>, Int>> =
			DataManager.getTopics(forumId, page,20)
					.map { getTopics(it) }

	private fun getTopicsFromDb(): Single<Pair<ArrayList<Forum.Topic>, Int>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getTopicsPath(forumId, page, 20))
					.map { it.response }
					.map { ForumResponse.Deserializer().deserialize(it) }
					.map { getTopics(it) }

	private fun getTopics(response: ForumResponse): Pair<ArrayList<Forum.Topic>, Int> = response.topics.items to response.topics.last

	override fun getCurrentPage(): Int = page

	override fun getPreviousTotal(): Int = previousTotal

	override fun setCurrentPage(page: Int) {
		this.page = page
	}

	override fun setPreviousTotal(previousTotal: Int) {
		this.previousTotal = previousTotal
	}

	override fun onItemClick(position: Int, v: View?, item: Forum.Topic) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Forum.Topic) {
		sendToView { it.onItemLongClicked(position, v, item) }
	}
}