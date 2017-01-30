package org.odddev.fantlab.award

import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.network.IServerApi
import org.odddev.fantlab.core.rx.ISchedulersResolver

import javax.inject.Inject

import rx.Single

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

class AwardsProvider : IAwardsProvider {

	@Inject
	lateinit var schedulersResolver: ISchedulersResolver

	@Inject
	lateinit var serverApi: IServerApi

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getAwards(): Single<List<Award>> {
		return serverApi.getAwards().compose(schedulersResolver.applyDefaultSchedulers<List<Award>>())
	}
}
