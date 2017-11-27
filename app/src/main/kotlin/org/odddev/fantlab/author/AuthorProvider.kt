package org.odddev.fantlab.author

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.odddev.fantlab.author.models.Author
import org.odddev.fantlab.core.db.MainDatabase
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.network.IServerApi
import javax.inject.Inject

class AuthorProvider : IAuthorProvider {

	@Inject
	lateinit var serverApi: IServerApi

	@Inject
	lateinit var database: MainDatabase

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getAuthor(id: Int): Flowable<Author> =
			serverApi.getAuthor(id)
					.flatMap { response ->
						run {
							database.authorPseudonymDao().saveAuthorPseudonymsFromResponse(response)
							database.authorStatDao().saveAuthorStatFromResponse(response)
							// todo сохранить авторов из ворков тоже
							database.authorDao().saveAuthorFromResponse(response)
						}
					}
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
}
