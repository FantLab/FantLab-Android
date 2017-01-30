package org.odddev.fantlab.home

import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.rx.ISchedulersResolver
import org.odddev.fantlab.core.storage.StorageManager

import javax.inject.Inject

import rx.Single

/**
 * @author kenrube
 * *
 * @since 11.12.16
 */

class HomeProvider : IHomeProvider {

	@Inject
	lateinit var storageManager: StorageManager

	@Inject
	lateinit var schedulersResolver: ISchedulersResolver

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getUserName(): Single<String> {
		return Single.just<String>(storageManager.loadUsername())
				.compose(schedulersResolver.applyDefaultSchedulers<String>())
	}

	override fun clearCookie() {
		storageManager.clearCookie()
	}
}
