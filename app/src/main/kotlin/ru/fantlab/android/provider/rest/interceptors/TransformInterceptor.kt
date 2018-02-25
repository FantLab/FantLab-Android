package ru.fantlab.android.provider.rest.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import ru.fantlab.android.data.dao.model.AuthorInList
import ru.fantlab.android.provider.rest.RestProvider

class TransformInterceptor : Interceptor {

	private val gson = RestProvider.gson

	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val response = chain.proceed(request)
		if (response.isSuccessful) {
			request.url().pathSegments().map {
				if (it.contains("autorsall")) {
					return interceptAuthors(response)
				}
			}
		}
		return response
	}

	private fun interceptAuthors(response: Response): Response {
		val body = response.body()!!.string()

		val authorsResponse = gson.fromJson(body, AuthorsResponse::class.java)
		val json = gson.toJson(authorsResponse.list)

		return response.newBuilder().body(ResponseBody.create(response.body()!!.contentType(), json)).build()
	}
}

data class AuthorsResponse(
		var list: List<AuthorInList>
)