package org.odddev.fantlab.home

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.storage.StorageManager
import javax.inject.Inject

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

	override fun getUserName(): Observable<String> {
		return Observable.just<String>(storageManager.loadUsername())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
	}

	override fun clearCookie() {
		storageManager.clearCookie()
	}
}
