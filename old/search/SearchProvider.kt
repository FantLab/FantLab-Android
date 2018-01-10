package ru.fantlab.android.old.search

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.fantlab.android.old.core.di.Injector
import ru.fantlab.android.old.core.network.IServerApi
import javax.inject.Inject

class SearchProvider : ISearchProvider {

	@Inject
	lateinit var serverApi: IServerApi

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun search(query: String) = serverApi.search(query)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
}
