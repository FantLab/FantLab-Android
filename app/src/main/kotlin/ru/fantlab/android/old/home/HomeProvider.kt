package ru.fantlab.android.old.home

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.fantlab.android.old.core.di.Injector
import ru.fantlab.android.old.core.storage.StorageManager
import javax.inject.Inject

class HomeProvider : IHomeProvider {

	@Inject
	lateinit var storageManager: StorageManager

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun getUserName() = Observable.just<String>(storageManager.loadUsername())
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())

	override fun clearCookie() {
		storageManager.clearCookie()
	}

	override fun clearUserName() {
		storageManager.clearUserName()
	}
}
