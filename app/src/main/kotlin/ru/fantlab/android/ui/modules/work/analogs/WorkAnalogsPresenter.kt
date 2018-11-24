package ru.fantlab.android.ui.modules.work.analogs

import android.os.Bundle
import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.data.dao.response.WorkAnalogsResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getWorkAnalogsPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class WorkAnalogsPresenter : BasePresenter<WorkAnalogsMvp.View>(),
		WorkAnalogsMvp.Presenter,
		BaseViewHolder.OnItemClickListener<WorkAnalog> {

	private var workId = -1

	override fun onFragmentCreated(bundle: Bundle) {
		workId = bundle.getInt(BundleConstant.EXTRA)
		getAnalogs(false)
	}

	override fun getAnalogs(force: Boolean) {
		makeRestCall(
				getAnalogsInternal(force).toObservable(),
				Consumer { analogs ->
					sendToView {
						with (it) {
							onSetTabCount(analogs.size)
							onInitViews(analogs)
						}
					}
				}
		)
	}

	private fun getAnalogsInternal(force: Boolean) =
			getAnalogsFromServer()
					.onErrorResumeNext { throwable ->
						if (!force) {
							getAnalogsFromDb()
						} else {
							throw throwable
						}
					}

	private fun getAnalogsFromServer(): Single<ArrayList<WorkAnalog>> =
			DataManager.getWorkAnalogs(workId)
					.map { getAnalogs(it) }

	private fun getAnalogsFromDb(): Single<ArrayList<WorkAnalog>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getWorkAnalogsPath(workId))
					.map { it.response }
					.map { WorkAnalogsResponse.Deserializer().deserialize(it) }
					.map { getAnalogs(it) }

	private fun getAnalogs(response: WorkAnalogsResponse): ArrayList<WorkAnalog> =
			response.analogs

	override fun onItemClick(position: Int, v: View?, item: WorkAnalog) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: WorkAnalog?) {
	}
}