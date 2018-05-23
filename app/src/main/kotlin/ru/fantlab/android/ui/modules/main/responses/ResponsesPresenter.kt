package ru.fantlab.android.ui.modules.main.responses

import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.newmodel.Response
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import java.util.*

class ResponsesPresenter : BasePresenter<ResponsesMvp.View>(), ResponsesMvp.Presenter {

	var responses: ArrayList<Response> = ArrayList()
	private var page: Int = 0
	private var previousTotal: Int = 0
	private var lastPage = Int.MAX_VALUE

	override fun onFragmentCreated() {
		if (responses.isEmpty()) {
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
		makeRestCall(
				DataManager.getLastResponses(page)
						.map { it.get() }
						.toObservable(),
				Consumer { response ->
					lastPage = response.responses.last
					if (getCurrentPage() == 1) {
						//manageDisposable(response.responses.items.save())
					}
					sendToView { it.onNotifyAdapter(response.responses.items, page) }
				}
		)
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
		sendToView { it.hideProgress() }
		sendToView { it.showErrorMessage("Не удалось загрузить данные") }
	}

	override fun onItemClick(position: Int, v: View?, item: Response) {
		// todo implement
	}

	override fun onItemLongClick(position: Int, v: View?, item: Response) {
		// todo implement
	}
}