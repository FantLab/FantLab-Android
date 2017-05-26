package org.odddev.fantlab.award

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.network.IServerApi
import javax.inject.Inject

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

class AwardsProvider : IAwardsProvider {

	@Inject
	lateinit var serverApi: IServerApi

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getAwards(): Observable<List<Award>> {
		return serverApi.getAwards(1)
				.subscribeOn(Schedulers.io())
	}
}
