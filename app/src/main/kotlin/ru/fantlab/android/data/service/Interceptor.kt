package ru.fantlab.android.data.service

import android.net.Uri
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.isSuccessful
import ru.fantlab.android.BuildConfig
import ru.fantlab.android.helper.PrefGetter
import ru.fantlab.android.provider.storage.DbProvider

fun dbResponseInterceptor() =
		{ next: (Request, Response) -> Response ->
			{ req: Request, res: Response ->
				if (res.isSuccessful) {
					val pageValue = Uri.parse(req.path).getQueryParameter("page")
					// пагинационные запросы для страниц дальше 1-й не сохраняем
					if (pageValue == null || pageValue == "1") {
						val response = ru.fantlab.android.data.db.response.Response(
								req.path,
								PrefGetter.getSessionUserId(),
								String(res.data),
								BuildConfig.API_VERSION,
								System.currentTimeMillis()
						)
						DbProvider.mainDatabase.responseDao().save(response)
					}
				}
				next(req, res)
			}
		}