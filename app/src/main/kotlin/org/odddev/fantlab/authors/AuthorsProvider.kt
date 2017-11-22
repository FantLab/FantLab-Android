package org.odddev.fantlab.authors

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.network.IServerApi
import javax.inject.Inject

class AuthorsProvider : IAuthorsProvider {

	@Inject
	lateinit var serverApi: IServerApi

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getAuthors() = serverApi.getAuthors()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
}
