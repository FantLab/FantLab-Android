package ru.fantlab.android.ui.modules.work.content

import android.os.Bundle
import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.ChildWork
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkContentPresenter : BasePresenter<WorkContentMvp.View>(),
		WorkContentMvp.Presenter {

	@com.evernote.android.state.State var editionId: Int? = null
	private var content: ArrayList<ChildWork> = ArrayList()

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or Work is null")
		}
		editionId = bundle.getInt(BundleConstant.EXTRA)
		editionId?.let {
			makeRestCall(
					DataManager.getWork(it, showChildren = true)
							.map { it.get() }
							.toObservable(),
					Consumer { workResponse ->
						sendToView { it.onInitViews(workResponse.children) }
					}
			)
		}
	}

	override fun onError(throwable: Throwable) {
		editionId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showMessage(R.string.error, R.string.failed_data) }
	}

	fun onCallApi() {
		editionId?.let {
			makeRestCall(
					DataManager.getEdition(it, showContent = true)
							.map { it.get() }
							.toObservable(),
					Consumer { workResponse ->
						sendToView { it.onNotifyAdapter() }
					}
			)
		}
	}

	override fun getContent(): ArrayList<ChildWork> = content

	override fun onItemClick(position: Int, v: View?, item: ChildWork) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: ChildWork) {
	}
}