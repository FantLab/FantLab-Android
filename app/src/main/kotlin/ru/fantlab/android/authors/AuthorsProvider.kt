package ru.fantlab.android.authors

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.fantlab.android.core.db.MainDatabase
import ru.fantlab.android.core.di.Injector
import ru.fantlab.android.core.network.IServerApi
import javax.inject.Inject

class AuthorsProvider : IAuthorsProvider {

	@Inject
	lateinit var serverApi: IServerApi

	@Inject
	lateinit var database: MainDatabase

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getAuthors(): Flowable<List<AuthorInList>> {
		serverApi.getAuthors()
				.subscribeOn(Schedulers.io())
				.subscribe {
					response -> database.authorDao().saveAuthorsFromResponse(response)
				}
		return database.authorDao()
				.getAllAsFlowable()
				.distinctUntilChanged()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
	}
}
