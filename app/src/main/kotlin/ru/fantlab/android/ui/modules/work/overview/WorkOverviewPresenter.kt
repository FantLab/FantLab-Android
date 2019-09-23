package ru.fantlab.android.ui.modules.work.overview

import android.os.Bundle
import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.data.dao.response.BookcaseInclusionResponse
import ru.fantlab.android.data.dao.response.BookcaseItemIncludedResponse
import ru.fantlab.android.data.dao.response.MarksMiniResponse
import ru.fantlab.android.data.dao.response.WorkResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.helper.FantlabHelper
import ru.fantlab.android.helper.Tuple4
import ru.fantlab.android.provider.rest.*
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class WorkOverviewPresenter : BasePresenter<WorkOverviewMvp.View>(), WorkOverviewMvp.Presenter {

	var workId = -1

	override fun onFragmentCreated(bundle: Bundle) {
		workId = bundle.getInt(BundleConstant.EXTRA)
		makeRestCall(
				getWorkInternal(workId).toObservable(),
				Consumer { (response, nominations, wins, authors) ->
					wins.addAll(nominations)
					sendToView { it.onInitViews(response.work, response.rootSagas, wins, authors) }
				}
		)
		getClassificatory()
		getEditions()
		getTranslations()
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
			DataManager.getWork(workId, showAwards = true, showTranslations = true)
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

	override fun getClassification() = getClassificatory()

	private fun getClassificatory() {
		makeRestCall(
				getClassificatoryInternal(workId).toObservable(),
				Consumer { classificatory -> sendToView { it.onSetClassification(classificatory) } }
		)
	}

	private fun getClassificatoryInternal(workId: Int) =
			getClassificatoryFromServer(workId)
					.onErrorResumeNext {
						getClassificatoryFromDb(workId)
					}

	private fun getClassificatoryFromServer(workId: Int): Single<ArrayList<GenreGroup>> =
			DataManager.getWork(workId, showClassificatory = true)
					.map { getClassificatory(it) }

	private fun getClassificatoryFromDb(workId: Int): Single<ArrayList<GenreGroup>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getWorkPath(workId, showClassificatory = true))
					.map { it.response }
					.map { WorkResponse.Deserializer().deserialize(it) }
					.map { getClassificatory(it) }

	private fun getClassificatory(response: WorkResponse): ArrayList<GenreGroup> =
			response.classificatory

	private fun getEditions() {
		makeRestCall(
				getEditionsInternal().toObservable(),
				Consumer { (editions) ->
					sendToView { it.onSetEditions(editions) }
				}
		)
	}

	private fun getEditionsInternal() =
			getEditionsFromServer()
					.onErrorResumeNext { getEditionsFromDb() }
					.onErrorResumeNext { ext -> Single.error(ext) }
					.doOnError { err -> sendToView { it.hideProgress() } }

	private fun getEditionsFromServer(): Single<EditionsBlocks> =
			DataManager.getWork(workId, showEditionsBlocks = true)
					.map { getEditions(it) }

	private fun getEditionsFromDb(): Single<EditionsBlocks> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getWorkPath(workId, showEditionsBlocks = true))
					.map { it.response }
					.map { WorkResponse.Deserializer().deserialize(it) }
					.map { getEditions(it) }

	private fun getEditions(response: WorkResponse): EditionsBlocks = response.editionsBlocks ?: EditionsBlocks(arrayListOf())

	fun getBookcases(bookcaseType: String, entityId: Int, force: Boolean) {
		makeRestCall(
				getBookcasesInternal(bookcaseType, entityId, force).toObservable(),
				Consumer { bookcasesInclusions ->
					sendToView {
						val inclusions: ArrayList<BookcaseSelection> = ArrayList()
						bookcasesInclusions!!.forEach { inclusions.add(BookcaseSelection(it, it.itemAdded == 1)) }
						it.onSetBookcases(inclusions)
						it.hideProgress()
					}
				}
		)
	}

	private fun getBookcasesInternal(bookcaseType: String, entityId: Int, force: Boolean) =
			getBookcasesFromServer(bookcaseType, entityId)
					.onErrorResumeNext { throwable ->
						if (!force) {
							getBookcasesFromDb(bookcaseType, entityId)
						} else {
							throw throwable
						}
					}

	private fun getBookcasesFromServer(bookcaseType: String, entityId: Int): Single<ArrayList<BookcaseInclusion>> =
			DataManager.getBookcaseInclusions(bookcaseType, entityId)
					.map { getBookcases(it) }

	private fun getBookcasesFromDb(bookcaseType: String, entityId: Int): Single<ArrayList<BookcaseInclusion>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getBookcaseInclusionsPath(bookcaseType, entityId))
					.map { it.response }
					.map { BookcaseInclusionResponse.Deserializer().deserialize(it) }
					.map { getBookcases(it) }

	private fun getBookcases(response: BookcaseInclusionResponse): ArrayList<BookcaseInclusion> {
		val inclusions = ArrayList<BookcaseInclusion>()
		response.items.forEach { inclusions.add(BookcaseInclusion(it.bookcaseId, it.bookcaseName, it.itemAdded, "work")) }
		return inclusions
	}

	fun includeItem(bookcaseId: Int, entityId: Int, include: Boolean) {
		makeRestCall(
				DataManager.includeItemToBookcase(bookcaseId, entityId, if (include) "add" else "delete").toObservable(),
				Consumer { response ->
					val result = BookcaseItemIncludedResponse.Parser().parse(response)
					sendToView {
						if (result == null) {
							it.showErrorMessage(response)
						} else {
							it.onBookcaseSelectionUpdated(bookcaseId, include)
						}
					}

				}
		)
	}

	private fun getTranslations() {
		makeRestCall(
				getTranslationsInternal().toObservable(),
				Consumer { translations ->
					sendToView { it.onSetTranslations(translations) }
				}
		)
	}

	private fun getTranslationsInternal() =
			getTranslationsFromServer()
					.onErrorResumeNext { getTranslationsFromDb() }
					.onErrorResumeNext { ext -> Single.error(ext) }
					.doOnError { err -> sendToView { it.hideProgress() } }

	private fun getTranslationsFromServer(): Single<ArrayList<Translation>> =
			DataManager.getWork(workId, showTranslations = true)
					.map { getTranslations(it) }

	private fun getTranslationsFromDb(): Single<ArrayList<Translation>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getWorkPath(workId, showTranslations = true))
					.map { it.response }
					.map { WorkResponse.Deserializer().deserialize(it) }
					.map { getTranslations(it) }

	private fun getTranslations(response: WorkResponse): ArrayList<Translation> = response.translations

	override fun onItemClick(position: Int, v: View?, item: Nomination) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Nomination) {
	}
}