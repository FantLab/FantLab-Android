package ru.fantlab.android.data.service

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.isSuccessful
import ru.fantlab.android.BuildConfig
import ru.fantlab.android.provider.storage.DbProvider

fun dbResponseInterceptor() =
		{ next: (Request, Response) -> Response ->
			{ req: Request, res: Response ->
				if (res.isSuccessful) {
					val response = ru.fantlab.android.data.db.response.Response(
							req.path,
							String(res.data),
							BuildConfig.API_VERSION,
							System.currentTimeMillis()
					)
					DbProvider.mainDatabase.responseDao().save(response)
				}
				next(req, res)
			}
		}