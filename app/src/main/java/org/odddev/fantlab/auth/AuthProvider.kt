package org.odddev.fantlab.auth

import android.text.TextUtils
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.network.IServerApi
import org.odddev.fantlab.core.rx.applyDefaultSchedulers
import org.odddev.fantlab.core.storage.StorageManager
import rx.Single
import javax.inject.Inject

/**
 * @author kenrube
 * *
 * @since 15.09.16
 */

class AuthProvider : IAuthProvider {

	@Inject
	lateinit var serverApi: IServerApi

	@Inject
	lateinit var storageManager: StorageManager

	init {
		Injector.getAppComponent().inject(this)
	}

	override fun login(username: String, password: String): Single<Boolean> {
		return serverApi.login(username, password)
				.map({ response ->
					val cookie = response.headers().get(COOKIE_HEADER)
					if (!TextUtils.isEmpty(cookie)) {
						storageManager.saveCookie(cookie)
					}
					// todo 6.32
					storageManager.saveUsername(username)
					!TextUtils.isEmpty(cookie)
				})
				.applyDefaultSchedulers()
	}

	override fun register(username: String, password: String, email: String): Single<Boolean> {
		return Single.just(true)
	}

	companion object {

		private val COOKIE_HEADER = "Set-Cookie"
	}
}
