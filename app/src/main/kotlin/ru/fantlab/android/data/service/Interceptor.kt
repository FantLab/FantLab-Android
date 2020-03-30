package ru.fantlab.android.data.service

import android.net.Uri
import com.github.kittinunf.fuel.core.FoldableResponseInterceptor
import com.github.kittinunf.fuel.core.ResponseTransformer
import com.github.kittinunf.fuel.core.isSuccessful
import ru.fantlab.android.BuildConfig
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.storage.DbProvider

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