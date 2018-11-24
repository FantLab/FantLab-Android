package ru.fantlab.android.ui.modules.work.content

import android.os.Bundle
import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.ChildWork
import ru.fantlab.android.data.dao.response.WorkResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getWorkPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkContentPresenter : BasePresenter<WorkContentMvp.View>(),
		WorkContentMvp.Presenter {

	private var workId = -1

	override fun onFragmentCreated(bundle: Bundle) {
		workId = bundle.getInt(BundleConstant.EXTRA)
		getContent(false)
	}

	override fun getContent(force: Boolean) {
		makeRestCall(
				getContentInternal(force).toObservable(),
				Consumer { content -> sendToView { it.onInitViews(content) } }
		)
	}

	private fun getContentInternal(force: Boolean) =
			getContentFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getContentFromDb()
						} else {
							throw throwable
						}
					}

	private fun getContentFromServer(): Single<ArrayList<ChildWork>> =
			DataManager.getWork(workId, showChildren = true)
					.map { getContent(it) }

	private fun getContentFromDb(): Single<ArrayList<ChildWork>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getWorkPath(workId, showChildren = true))
					.map { it.response }
					.map { WorkResponse.Deserializer().deserialize(it) }
					.map { getContent(it) }

	private fun getContent(response: WorkResponse): ArrayList<ChildWork> = response.children

	override fun onItemClick(position: Int, v: View?, item: ChildWork) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: ChildWork) {
	}
}