package org.odddev.fantlab.authors

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.odddev.fantlab.core.db.MainDatabase
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.network.IServerApi
import javax.inject.Inject

class AuthorsProvider : IAuthorsProvider {

	@Inject
	lateinit var serverApi: IServerApi

	@Inject
	lateinit var database: MainDatabase

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getAuthors() = Flowable.merge(
			database.authorDao()
					.getByOrder()
					.distinctUntilChanged()
					.subscribeOn(Schedulers.io()),
			serverApi.getAuthors()
					.flatMap { response -> database.authorDao().saveAuthorsFromResponse(response) }
					.subscribeOn(Schedulers.io())
	).observeOn(AndroidSchedulers.mainThread())
}
