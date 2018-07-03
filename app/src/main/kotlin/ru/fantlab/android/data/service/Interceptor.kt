package ru.fantlab.android.data.service

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import ru.fantlab.android.BuildConfig
import ru.fantlab.android.provider.storage.DbProvider

fun dbResponseInterceptor() =
		{ next: (Request, Response) -> Response ->
			{ req: Request, res: Response ->
				val response = ru.fantlab.android.data.db.response.Response(
						req.path,
						BuildConfig.API_VERSION,
						String(res.data)
				)
				DbProvider.responseDatabase.responseDao().save(response)
				next(req, res)
			}
		}