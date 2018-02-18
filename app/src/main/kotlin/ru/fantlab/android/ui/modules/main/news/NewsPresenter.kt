package ru.fantlab.android.ui.modules.main.news

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.data.dao.model.getNews
import ru.fantlab.android.data.dao.model.save
import ru.fantlab.android.helper.observe
import ru.fantlab.android.provider.StubProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import timber.log.Timber
import java.util.*

class NewsPresenter : BasePresenter<NewsMvp.View>(), NewsMvp.Presenter {

	var news: ArrayList<News> = ArrayList()
	private var page: Int = 0
	private var previousTotal: Int = 0
	private var lastPage = Int.MAX_VALUE

	override fun onFragmentCreated() {
		if (news.isEmpty()) {
			onCallApi(1)
		}
	}

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
		makeRestCall(/*RestProvider.getCommonService()*/StubProvider.getNews(page), Consumer {
			lastPage = it.last
			val items = it.items
			if (getCurrentPage() == 1) {
				manageDisposable(items.save())
			}
			sendToView { it.onNotifyAdapter(items, page) }
			sendToView { it.showErrorMessage("API not ready yet") }
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

	override fun onWorkOffline() {
		if (news.isEmpty()) {
			manageDisposable(
					getNews().toObservable().observe().subscribe(
							{ modelList ->
								modelList?.let {
									sendToView { it.onNotifyAdapter(modelList, 1) }
								}
							},
							Timber::e
					)
			)
		} else {
			sendToView { it.hideProgress() }
		}
	}

	override fun onItemClick(position: Int, v: View?, item: News?) {
		// todo implement
	}

	override fun onItemLongClick(position: Int, v: View?, item: News?) {
		// todo implement
	}
}