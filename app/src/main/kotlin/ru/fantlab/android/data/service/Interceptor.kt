package ru.fantlab.android.data.service

import android.net.Uri
import com.github.kittinunf.fuel.core.*
import ru.fantlab.android.BuildConfig
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.helper.format
import ru.fantlab.android.provider.storage.DbProvider
import timber.log.Timber

object LogReqRespInterceptor : FoldableResponseInterceptor {
	override fun invoke(next: ResponseTransformer): ResponseTransformer {
		return { request, response ->
			if (request.url.toString().contains("login")) {
				Timber.e("${request.format()}\n${response.format()}")
			}
			next(request, response)
		}
	}
}

object DbResponseInterceptor : FoldableResponseInterceptor {
	override fun invoke(next: ResponseTransformer): ResponseTransformer {
		return { request, response ->
			if (response.isSuccessful) {
				val pageValue = Uri.parse(request.url.toExternalForm()).getQueryParameter("page")
				// пагинационные запросы для страниц дальше 1-й не сохраняем
				if (pageValue == null || pageValue == "1") {
					val dbResponse = ru.fantlab.android.data.db.response.Response(
							request.url.toExternalForm(),
							PrefGetter.getSessionUserId(),
							String(response.data),
							BuildConfig.API_VERSION,
							System.currentTimeMillis()
					)
					DbProvider.mainDatabase.responseDao().save(dbResponse)
				}
			}
			next(request, response)
		}
	}
}