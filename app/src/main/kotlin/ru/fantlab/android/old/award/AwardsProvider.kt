package ru.fantlab.android.old.award

import io.reactivex.schedulers.Schedulers
import ru.fantlab.android.old.core.di.Injector
import ru.fantlab.android.old.core.network.IServerApi
import javax.inject.Inject

class AwardsProvider : IAwardsProvider {

	@Inject
	lateinit var serverApi: IServerApi

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getAwards() = serverApi.getAwards(1).subscribeOn(Schedulers.io())
}
