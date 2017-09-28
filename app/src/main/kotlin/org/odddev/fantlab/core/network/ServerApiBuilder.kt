package org.odddev.fantlab.core.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.odddev.fantlab.autors.Autor
import org.odddev.fantlab.autors.AutorDeserializer
import org.odddev.fantlab.autors.autor.AuthorPageInfo
import org.odddev.fantlab.autors.autor.AutorFull
import org.odddev.fantlab.autors.autor.AutorFullDeserializer
import org.odddev.fantlab.autors.autor.AuthorPageInfoDeserializer
import org.odddev.fantlab.award.Award
import org.odddev.fantlab.award.AwardDeserializer
import org.odddev.fantlab.core.Const
import org.odddev.fantlab.edition.Edition
import org.odddev.fantlab.edition.EditionDeserializer
import org.odddev.fantlab.work.Work
import org.odddev.fantlab.work.WorkDeserializer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

internal object ServerApiBuilder {

	private val HTTP_LOG_TAG = "OkHttp"

	fun createApi(): IServerApi {
		val builder = OkHttpClient.Builder()

		builder.addInterceptor(HeaderInterceptor())
		builder.addInterceptor(StethoInterceptor())

		val loggingInterceptor = HttpLoggingInterceptor { message -> Timber.tag(HTTP_LOG_TAG).d(message) }
		loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
		builder.addInterceptor(loggingInterceptor)

		builder.followRedirects(false)

		val httpClient = builder.build()

		val retrofitBuilder = Retrofit.Builder()
				.baseUrl(Const.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create(createGson()))
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.client(httpClient)

		return retrofitBuilder.build().create(IServerApi::class.java)
	}

	private fun createGson(): Gson = GsonBuilder()
			.registerTypeAdapter(AuthorPageInfo::class.java, AuthorPageInfoDeserializer())
			.registerTypeAdapter(Award::class.java, AwardDeserializer())
			.registerTypeAdapter(Autor::class.java, AutorDeserializer())
			.registerTypeAdapter(AutorFull::class.java, AutorFullDeserializer())
			.registerTypeAdapter(Work::class.java, WorkDeserializer())
			.registerTypeAdapter(Edition::class.java, EditionDeserializer())
			.create()
}
