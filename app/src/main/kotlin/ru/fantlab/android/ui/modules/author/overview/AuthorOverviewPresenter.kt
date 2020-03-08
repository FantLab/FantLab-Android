package ru.fantlab.android.ui.modules.author.overview

import android.os.Bundle
import android.view.View
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.data.dao.response.AuthorResponse
import ru.fantlab.android.helper.BundleConstant
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.getAuthorPath
import ru.fantlab.android.provider.storage.DbProvider
import ru.fantlab.android.ui.base.mvp.presenter.BasePresenter

class AuthorOverviewPresenter : BasePresenter<AuthorOverviewMvp.View>(),
		AuthorOverviewMvp.Presenter {

	data class AuthorTuple(
		val author: Author,
		val biography: Biography?,
		val classificatory: ArrayList<GenreGroup>,
		val awards: ArrayList<Nomination>
	)

	override fun onFragmentCreated(bundle: Bundle) {
		val authorId = bundle.getInt(BundleConstant.EXTRA)
		makeRestCall(
				getAuthorInternal(authorId).toObservable(),
				Consumer { (author, biography, classificatory, awards) -> sendToView { it.onInitViews(author, biography, classificatory, awards) } }
		)
	}

	private fun getAuthorInternal(authorId: Int) =
			getAuthorFromServer(authorId)
					.onErrorResumeNext {
						getAuthorFromDb(authorId)
					}
					.onErrorResumeNext { ext -> Single.error(ext) }
					.doOnError { err -> sendToView { it.onShowErrorView(err.message) } }

	private fun getAuthorFromServer(authorId: Int): Single<AuthorTuple> =
			DataManager.getAuthor(authorId, showBiography = true, showClassificatory = true, showAwards = true)
					.map { getAuthor(it) }

	private fun getAuthorFromDb(authorId: Int): Single<AuthorTuple> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getAuthorPath(authorId, showBiography = true, showClassificatory = true, showAwards = true))
					.map { it.response }
					.map { AuthorResponse.Deserializer().deserialize(it) }
					.map { getAuthor(it) }

	private fun getAuthor(response: AuthorResponse): AuthorTuple = AuthorTuple(
			response.author,
			response.biography,
			response.classificatory,
			response.awards?.wins ?: arrayListOf()
		)

	override fun onItemClick(position: Int, v: View?, item: Nomination) {
		sendToView { it.onItemClicked(item) }
	}

	override fun onItemLongClick(position: Int, v: View?, item: Nomination) {
	}

}