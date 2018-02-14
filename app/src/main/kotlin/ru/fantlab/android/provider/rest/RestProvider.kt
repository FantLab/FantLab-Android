package ru.fantlab.android.provider.rest

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.fantlab.android.BuildConfig
import ru.fantlab.android.data.service.CommonRestService
import ru.fantlab.android.data.service.SearchService
import ru.fantlab.android.data.service.UserRestService
import ru.fantlab.android.provider.rest.interceptors.AuthenticationInterceptor
import ru.fantlab.android.provider.rest.interceptors.PaginationInterceptor
import java.lang.reflect.Modifier

object RestProvider {

	private var okHttpClient: OkHttpClient? = null
	private val gson = GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
			.setDateFormat("yyyy-MM-dd HH:mm:ss")
			.disableHtmlEscaping()
			.setPrettyPrinting()
			.create()

	private fun provideOkHttpClient(): OkHttpClient {
		if (okHttpClient == null) {
			val client = OkHttpClient.Builder()
			if (BuildConfig.DEBUG) {
				client.addInterceptor(HttpLoggingInterceptor()
						.setLevel(HttpLoggingInterceptor.Level.BODY))
			}
			client.addInterceptor(AuthenticationInterceptor())
			client.addInterceptor(PaginationInterceptor())
			okHttpClient = client.build()
		}
		return okHttpClient!!
	}

	private fun provideRetrofit(): Retrofit {
		return Retrofit.Builder()
				.baseUrl(BuildConfig.REST_URL)
				.client(provideOkHttpClient())
				.addConverterFactory(GsonConverterFactory.create(gson))
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build()
	}

	fun getSearchService(): SearchService = provideRetrofit().create(SearchService::class.java)

	fun getUserService(): UserRestService = provideRetrofit().create(UserRestService::class.java)

	fun getCommonService(): CommonRestService = provideRetrofit().create(CommonRestService::class.java)
}