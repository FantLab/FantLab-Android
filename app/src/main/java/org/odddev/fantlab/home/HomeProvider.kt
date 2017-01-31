package org.odddev.fantlab.home

import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.rx.applyDefaultSchedulers
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

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getUserName(): Single<String> {
		return Single.just<String>(storageManager.loadUsername())
				.applyDefaultSchedulers()
	}

	override fun clearCookie() {
		storageManager.clearCookie()
	}
}
