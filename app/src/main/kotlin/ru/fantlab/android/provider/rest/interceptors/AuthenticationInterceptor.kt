package ru.fantlab.android.provider.rest.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.fantlab.android.BuildConfig
import ru.fantlab.android.helper.PrefGetter
import java.io.IOException

class AuthenticationInterceptor : Interceptor {

	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response {
		val original = chain.request()
		val builder = original.newBuilder()
		builder.header("User-Agent", "FantLab for Android v${BuildConfig.VERSION_NAME}")
		val token = PrefGetter.getToken()
		token?.let {
			builder.header("Cookie", it)
		}
		val request = builder.build()
		return chain.proceed(request)
	}
}