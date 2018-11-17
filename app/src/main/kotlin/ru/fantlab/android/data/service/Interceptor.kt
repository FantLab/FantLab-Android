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
						String(res.data),
						BuildConfig.API_VERSION
				)
				DbProvider.mainDatabase.responseDao().save(response)
				next(req, res)
			}
		}