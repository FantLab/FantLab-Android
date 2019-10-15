package ru.fantlab.android.ui.modules.author.overview

import android.os.Bundle
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

	override fun onFragmentCreated(bundle: Bundle) {
		val authorId = bundle.getInt(BundleConstant.EXTRA)
		makeRestCall(
				getAuthorInternal(authorId).toObservable(),
				Consumer { (author, biography, classificatory) -> sendToView { it.onInitViews(author, biography, classificatory) } }
		)
	}

	private fun getAuthorInternal(authorId: Int) =
			getAuthorFromServer(authorId)
					.onErrorResumeNext {
						getAuthorFromDb(authorId)
					}
					.onErrorResumeNext { ext -> Single.error(ext) }
					.doOnError { err -> sendToView { it.onShowErrorView(err.message) } }

	private fun getAuthorFromServer(authorId: Int): Single<Triple<Author, Biography?, ArrayList<GenreGroup>>> =
			DataManager.getAuthor(authorId, showBiography = true, showClassificatory = true)
					.map { getAuthor(it) }

	private fun getAuthorFromDb(authorId: Int): Single<Triple<Author, Biography?, ArrayList<GenreGroup>>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getAuthorPath(authorId, showBiography = true, showClassificatory = true))
					.map { it.response }
					.map { AuthorResponse.Deserializer().deserialize(it) }
					.map { getAuthor(it) }

	private fun getAuthor(response: AuthorResponse): Triple<Author, Biography?, ArrayList<GenreGroup>> = Triple(response.author, response.biography, response.classificatory)
}