package org.odddev.fantlab.core.network

import org.odddev.fantlab.BuildConfig
import org.odddev.fantlab.core.di.Injector
import org.odddev.fantlab.core.storage.StorageManager

import java.io.IOException

import javax.inject.Inject

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author kenrube
 * *
 * @since 15.09.16
 */

class HeaderInterceptor : Interceptor {

	@Inject
	lateinit var storageManager: StorageManager

	init {
		Injector.getAppComponent().inject(this)
	}

	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response {

		val cookie = storageManager.loadCookie()

		val original = chain.request()
		val builder = original.newBuilder()
				.header("Content-Type", CONTENT_TYPE)
				.header("User-Agent", APP_VERSION)
		if (cookie != null) builder.header("Cookie", cookie)
		builder.method(original.method(), original.body())
		return chain.proceed(builder.build())
	}

	companion object {

		private val CONTENT_TYPE = "application/x-www-form-urlencoded"
		private val APP_VERSION = "Android/" + BuildConfig.VERSION_NAME
	}
}
