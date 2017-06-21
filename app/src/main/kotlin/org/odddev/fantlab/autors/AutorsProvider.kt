package org.odddev.fantlab.autors

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.network.IServerApi
import javax.inject.Inject

class AutorsProvider : IAutorsProvider {

	@Inject
	lateinit var serverApi: IServerApi

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getAutors() = serverApi.getAutors()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
}
