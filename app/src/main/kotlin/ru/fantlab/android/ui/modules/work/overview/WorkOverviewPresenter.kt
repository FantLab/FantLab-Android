package ru.fantlab.android.ui.modules.work.overview

import android.os.Bundle
import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.MarkMini
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.data.dao.model.Work
import ru.fantlab.android.data.dao.response.MarksMiniResponse
import ru.fantlab.android.data.dao.response.WorkResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.helper.Tuple4
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getUserMarksMiniPath
import ru.fantlab.android.provider.rest.getWorkPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkOverviewPresenter : BasePresenter<WorkOverviewMvp.View>(), WorkOverviewMvp.Presenter {

	override fun onFragmentCreated(bundle: Bundle) {
		val workId = bundle.getInt(BundleConstant.EXTRA)
		makeRestCall(
				getWorkInternal(workId).toObservable(),
				Consumer { (response, nominations, wins, authors) ->
					sendToView { it.onInitViews(response.work, response.rootSagas, nominations, wins, authors) }
				}
		)
	}

	private fun getWorkInternal(workId: Int) =
			getWorkFromServer(workId)
					.onErrorResumeNext {
						getWorkFromDb(workId)
					}
					.onErrorResumeNext { ext -> Single.error(ext) }
					.doOnError { err -> sendToView { it.onShowErrorView(err.message) } }

	private fun getWorkFromServer(workId: Int):
			Single<Tuple4<WorkResponse, ArrayList<Nomination>, ArrayList<Nomination>, ArrayList<Work.Author>>> =
			DataManager.getWork(workId, showAwards = true)
					.map { getWork(it) }

	private fun getWorkFromDb(workId: Int):
			Single<Tuple4<WorkResponse, ArrayList<Nomination>, ArrayList<Nomination>, ArrayList<Work.Author>>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getWorkPath(workId, showAwards = true, showParents = true))
					.map { it.response }
					.map { WorkResponse.Deserializer().deserialize(it) }
					.map { getWork(it) }

	private fun getWork(response: WorkResponse):
			Tuple4<WorkResponse, ArrayList<Nomination>, ArrayList<Nomination>, ArrayList<Work.Author>> =
			Tuple4(
					response,
					response.awards?.nominations ?: arrayListOf(),
					response.awards?.wins ?: arrayListOf(),
					ArrayList(response.work.authors.filter { it.id !in FantlabHelper.Authors.ignoreList })
			)

	override fun getMarks(userId: Int, workIds: ArrayList<Int>) {
		makeRestCall(
				getMarksInternal(userId, workIds).toObservable(),
				Consumer { marks ->
					sendToView { it.onGetMarks(marks) }
				}
		)
	}

	private fun getMarksInternal(userId: Int, workIds: ArrayList<Int>) =
			getMarksFromServer(userId, workIds)
					.onErrorResumeNext {
						getMarksFromDb(userId, workIds)
					}

	private fun getMarksFromServer(userId: Int, workIds: ArrayList<Int>): Single<ArrayList<MarkMini>> =
			DataManager.getUserMarksMini(userId, workIds.joinToString())
					.map { getMarks(it) }

	private fun getMarksFromDb(userId: Int, workIds: ArrayList<Int>): Single<ArrayList<MarkMini>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getUserMarksMiniPath(userId, workIds.joinToString()))
					.map { it.response }
					.map { MarksMiniResponse.Deserializer().deserialize(it) }
					.map { getMarks(it) }

	private fun getMarks(response: MarksMiniResponse): ArrayList<MarkMini> = response.marks

	override fun onSendMark(workId: Int, mark: Int) {
		makeRestCall(
				DataManager.sendUserMark(workId, workId, mark).toObservable(),
				Consumer { response ->
					sendToView {
						it.onSetMark(mark, response.markCount, response.midMark)
					}
				}
		)
	}

	override fun onItemClick(position: Int, v: View?, item: Nomination) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Nomination) {
	}
}