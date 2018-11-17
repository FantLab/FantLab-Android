package ru.fantlab.android.ui.modules.work.editions

import android.os.Bundle
import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.EditionsBlocks
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkEditionsPresenter : BasePresenter<WorkEditionsMvp.View>(),
		WorkEditionsMvp.Presenter {

	@com.evernote.android.state.State var workId: Int? = null
	private var editions: ArrayList<EditionsBlocks.Edition> = ArrayList()

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or Work is null")
		}
		workId = bundle.getInt(BundleConstant.EXTRA)
		workId?.let {
			makeRestCall(
					DataManager.getWork(it, showEditionsBlocks = true, showEditionsInfo = true)
							.toObservable(),
					Consumer { workResponse ->
						sendToView {
							it.onInitViews(workResponse.editionsBlocks, workResponse.editionsInfo!!)
						}
					}
			)
		}
	}

	override fun onError(throwable: Throwable) {
		workId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showMessage(R.string.error, R.string.failed_data) }
	}

	override fun getEditions(): ArrayList<EditionsBlocks.Edition> = editions

	fun onCallApi() {
		workId?.let {
			makeRestCall(
					DataManager.getWork(it, showEditionsBlocks = true, showEditionsInfo = true)
							.toObservable(),
					Consumer { workResponse ->
						sendToView { it.onNotifyAdapter() }
					}
			)
		}
	}

	override fun onItemClick(position: Int, v: View?, item: EditionsBlocks.Edition) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: EditionsBlocks.Edition) {
	}
}