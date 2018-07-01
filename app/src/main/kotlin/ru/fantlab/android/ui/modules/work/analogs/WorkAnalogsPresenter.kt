package ru.fantlab.android.ui.modules.work.analogs

import android.os.Bundle
import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.widgets.recyclerview.BaseViewHolder

class WorkAnalogsPresenter : BasePresenter<WorkAnalogsMvp.View>(),
		WorkAnalogsMvp.Presenter,
        BaseViewHolder.OnItemClickListener<WorkAnalog>{

	@com.evernote.android.state.State
	var workId: Int? = null
    private var analogs: ArrayList<WorkAnalog> = ArrayList()

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or Work is null")
		}
		workId = bundle.getInt(BundleConstant.EXTRA)
		workId?.let {
			makeRestCall(
					DataManager.getWorkAnalogs(it)
							.map { it.get() }
							.toObservable(),
					Consumer { workAnalogsResponse ->
						sendToView {
                            it.onSetTabCount(workAnalogsResponse.analogs.size)
                            it.onInitViews(workAnalogsResponse.analogs)
                        }
					}
			)}
	}

    override fun onItemClick(position: Int, v: View?, item: WorkAnalog) {
        view?.onItemClicked(item)
    }

    override fun onItemLongClick(position: Int, v: View?, item: WorkAnalog?) {
    }

	override fun onError(throwable: Throwable) {
		workId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showErrorMessage("Не удалось загрузить данные") }
	}

    override fun getAnalogs(): ArrayList<WorkAnalog> = analogs

    fun onCallApi() {
        workId?.let {
            makeRestCall(
                    DataManager.getWorkAnalogs(it)
                            .map { it.get() }
                            .toObservable(),
                    Consumer { workResponse ->
                        sendToView { it.onNotifyAdapter() }
                    }
            )}
    }
}