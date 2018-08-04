package ru.fantlab.android.ui.modules.work.overview

import android.os.Bundle
import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkOverviewPresenter : BasePresenter<WorkOverviewMvp.View>(),
		WorkOverviewMvp.Presenter {

	@com.evernote.android.state.State
	var workId: Int? = null
	private var noms: ArrayList<Nomination>? = ArrayList()
	private var wins: ArrayList<Nomination>? = ArrayList()

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or Work is null")
		}
		workId = bundle.getInt(BundleConstant.EXTRA)
		workId?.let { it ->
			makeRestCall(
					DataManager.getWork(it, showAwards = true)
							.map { it.get() }
							.toObservable(),
					Consumer { workResponse ->
						sendToView {
							noms = workResponse.awards?.nominations
							wins = workResponse.awards?.wins
							it.onInitViews(workResponse.work) }
					}
			)}
	}

	override fun onError(throwable: Throwable) {
		workId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showErrorMessage("Не удалось загрузить данные") }
	}

	override fun onClick(v: View?) {
		sendToView { it.onClick(v) }
	}

	override fun getNoms(): ArrayList<Nomination>? = noms ?: ArrayList()

	override fun getWins(): ArrayList<Nomination>? = wins ?: ArrayList()

}