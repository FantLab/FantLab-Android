package org.odddev.fantlab.authors

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.intellij.lang.annotations.Flow
import org.odddev.fantlab.R.id.name
import org.odddev.fantlab.author.models.Author
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
					.get()
					.distinctUntilChanged()
					.subscribeOn(Schedulers.io()),
			serverApi.getAuthors()
					.flatMap { response -> database.authorDao().saveFromNetwork(response) }
					.subscribeOn(Schedulers.io())
	).observeOn(AndroidSchedulers.mainThread())
}
