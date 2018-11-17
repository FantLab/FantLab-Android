package ru.fantlab.android.ui.modules.work.overview

import android.os.Bundle
import android.view.View
import io.reactivex.functions.Consumer
import ru.fantlab.android.R
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.data.dao.model.Work
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkOverviewPresenter : BasePresenter<WorkOverviewMvp.View>(),
		WorkOverviewMvp.Presenter {

	@com.evernote.android.state.State var workId: Int? = null
	private var noms: ArrayList<Nomination>? = ArrayList()
	private var wins: ArrayList<Nomination>? = ArrayList()
	private var authors: ArrayList<Work.Author>? = ArrayList()

	override fun onFragmentCreated(bundle: Bundle?) {
		if (bundle?.getInt(BundleConstant.EXTRA) == null) {
			throw NullPointerException("Either bundle or Work is null")
		}
		workId = bundle.getInt(BundleConstant.EXTRA)
		workId?.let { it ->
			makeRestCall(
					DataManager.getWork(it, showAwards = true)
							.toObservable(),
					Consumer { workResponse ->
						sendToView { it ->
							noms = workResponse.awards?.nominations
							wins = workResponse.awards?.wins
							authors = workResponse.work.authors
									.filter { it.id !in FantlabHelper.Authors.ignoreList } as ArrayList
							it.onInitViews(workResponse.work)
						}
					}
			)
		}
	}

	fun onSendMark(workId: Int, mark: Int) {
		makeRestCall(DataManager.sendUserMark(workId, workId, mark)
				.toObservable(),
				Consumer {
					sendToView { view ->
						view.onSetMark(mark, it.markCount, it.midMark)
					}
				})
	}

	override fun getMarks(userId: Int?, workIds: ArrayList<Int?>) {
		makeRestCall(DataManager.getUserMarksMini(userId!!, workIds.joinToString())
				.toObservable(),
				Consumer {
					sendToView { view ->
						view?.onGetMarks(it.marks)
					}
				})
	}

	override fun onError(throwable: Throwable) {
		workId?.let { onWorkOffline(it) }
		super.onError(throwable)
	}

	override fun onWorkOffline(id: Int) {
		sendToView { it.showMessage(R.string.error, R.string.failed_data) }
	}

	override fun onClick(v: View?) {
		sendToView { it.onClick(v) }
	}

	override fun onItemClick(position: Int, v: View?, item: Nomination) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Nomination) {
	}

	override fun getNoms(): ArrayList<Nomination>? = noms ?: ArrayList()

	override fun getWins(): ArrayList<Nomination>? = wins ?: ArrayList()

	override fun getAuthors(): ArrayList<Work.Author>? = authors ?: ArrayList()
}