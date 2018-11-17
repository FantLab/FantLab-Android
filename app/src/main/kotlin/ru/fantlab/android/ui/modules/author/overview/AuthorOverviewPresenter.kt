package ru.fantlab.android.ui.modules.author.overview

import android.os.Bundle
import io.reactivex.Single
import io.reactivex.functions.Consumer
import ru.fantlab.android.data.dao.model.Author
import ru.fantlab.android.data.dao.model.Biography
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
				Consumer { (author, biography) -> sendToView { it.onInitViews(author, biography) } }
		)
	}
	
	private fun getAuthorInternal(authorId: Int) =
			getAuthorFromServer(authorId)
					.onErrorResumeNext {
						getAuthorFromDb(authorId)
					}

	private fun getAuthorFromServer(authorId: Int): Single<Pair<Author, Biography?>> =
			DataManager.getAuthor(authorId, showBiography = true)
					.map { getAuthor(it) }

	private fun getAuthorFromDb(authorId: Int): Single<Pair<Author, Biography?>> =
			DbProvider.mainDatabase
					.responseDao()
					.get(getAuthorPath(authorId, showBiography = true))
					.map { it.toNullable()!!.response }
					.map { AuthorResponse.Deserializer().deserialize(it) }
					.map { getAuthor(it) }

	private fun getAuthor(response: AuthorResponse): Pair<Author, Biography?> =
			response.author to response.biography
}