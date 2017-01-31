package org.odddev.fantlab.award

import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.network.IServerApi
import org.odddev.fantlab.core.rx.applyDefaultSchedulers
import rx.Single
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

	override fun getAwards(): Single<List<Award>> {
		return serverApi.getAwards()
				.applyDefaultSchedulers()
	}
}
