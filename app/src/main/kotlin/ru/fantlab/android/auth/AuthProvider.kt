package ru.fantlab.android.auth

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.fantlab.android.core.di.Injector
import ru.fantlab.android.core.network.IServerApi
import ru.fantlab.android.core.storage.StorageManager
import javax.inject.Inject

class AuthProvider : IAuthProvider{

	@Inject
	lateinit var serverApi: IServerApi

	@Inject
	lateinit var storageManager: StorageManager

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun login(username: String, password: String): Flowable<Boolean> {
		return serverApi.login(username, password)
				.map({ response ->
					run {
						val cookies = response.headers().values("Set-Cookie")
						for (cookie in cookies) {
							if (cookie.startsWith("fl_s")) {
								storageManager.saveCookie(cookie)
								storageManager.saveUsername(username)
								return@run true
							}
						}
						return@run false
					}
				})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
	}
}