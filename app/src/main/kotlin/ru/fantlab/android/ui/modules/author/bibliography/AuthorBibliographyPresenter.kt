package ru.fantlab.android.ui.modules.author.bibliography

import android.os.Bundle
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.MarkMini
import ru.fantlab.android.data.dao.model.WorksBlocks
import ru.fantlab.android.data.dao.response.AuthorResponse
import ru.fantlab.android.data.dao.response.MarksMiniResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getAuthorPath
import ru.fantlab.android.provider.rest.getUserMarksMiniPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter
import ru.fantlab.android.ui.widgets.treeview.TreeNode

class AuthorBibliographyPresenter : BasePresenter<AuthorBibliographyMvp.View>(),
		AuthorBibliographyMvp.Presenter {

	override fun onFragmentCreated(bundle: Bundle) {
		val authorId = bundle.getInt(BundleConstant.EXTRA)
		makeRestCall(
				getAuthorInternal(authorId).toObservable(),
				Consumer { (cycles, works) -> sendToView { it.onInitViews(cycles, works) } }
		)
	}

	private fun getAuthorInternal(authorId: Int) =
			getAuthorFromServer(authorId)
					.onErrorResumeNext {
						getAuthorFromDb(authorId)
					}

	private fun getAuthorFromServer(authorId: Int): Single<Pair<WorksBlocks?, WorksBlocks?>> =
			DataManager.getAuthor(authorId, showBiblioBlocks = true)
					.map { getAuthor(it) }

	private fun getAuthorFromDb(authorId: Int): Single<Pair<WorksBlocks?, WorksBlocks?>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getAuthorPath(authorId, showBiblioBlocks = true))
					.map { it.toNullable()!!.response }
					.map { AuthorResponse.Deserializer().deserialize(it) }
					.map { getAuthor(it) }

	private fun getAuthor(response: AuthorResponse): Pair<WorksBlocks?, WorksBlocks?> =
			response.cycles to response.works

	override fun getMarks(userId: Int, workIds: ArrayList<Int>) {
		makeRestCall(
				getMarksInternal(userId, workIds).toObservable(),
				Consumer { marks -> sendToView { it.onGetMarks(marks) } }
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
					.map { it.toNullable()!!.response }
					.map { MarksMiniResponse.Deserializer().deserialize(it) }
					.map { getMarks(it) }

	private fun getMarks(response: MarksMiniResponse): ArrayList<MarkMini> = response.marks

	override fun onSendMark(workId: Int, mark: Int, position: Int) {
		makeRestCall(
				setMarkInternal(workId, mark).toObservable(),
				Consumer { _ -> sendToView { it.onSetMark(position, mark) } }
		)
	}

	private fun setMarkInternal(workId: Int, mark: Int): Single<Unit> =
			DataManager.sendUserMark(workId, workId, mark)
					.map { /* do nothing*/ }

	override fun onItemLongClick(item: TreeNode<*>, position: Int) {
		sendToView { it.onItemLongClicked(item, position) }
	}
}