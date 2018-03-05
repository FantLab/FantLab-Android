package ru.fantlab.android.provider.rest.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody

/**
 * Задача - сформировать объект вида
 * @sample
 * {
 *     last: Int,                     # последняя страница
 *     total_count: Int,              # всего результатов
 *     incomplete_results: Boolean,   # неполные результаты?
 *                                    # (на следующей странице результатов уже не будет, поскольку API отдать больше не может)
 *     matches: [                     # массив результатов
 *         ...
 *     ]
 * }
 */
class PaginationInterceptor : Interceptor {

	companion object {

		private val SEARCH_RESULTS_PER_PAGE = 25
		private val RESPONSES_PER_PAGE = 50
		private val LAST_RESPONSES_MAX_PAGE = 20
	}

	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val response = chain.proceed(request)
		if (response.isSuccessful) {
			request.url().pathSegments().map {
				if (it.contains("search-")) {
					return interceptSearch(request, response)
				} else if (it.contains("responses")) {
					return interceptResponses(
							request,
							response,
							request.url().pathSegments().size == 1
					)
				}
			}
		}
		return response
	}

	private fun interceptSearch(request: Request, response: Response): Response {
		val body = response.body()!!.string()

		val totalStartIndex = body.indexOf("\"total\":")
		val totalEndIndex = body.indexOf(",", startIndex = totalStartIndex)
		val totalCount = body.substring(totalStartIndex + "\"total\":".length, totalEndIndex).toInt()

		val lastPage = (totalCount - 1) / SEARCH_RESULTS_PER_PAGE
		val lastString = "\"last\":${lastPage + 1}"

		var currentPage = request.url().queryParameter("page")?.toInt()
		if (currentPage == null) {
			currentPage = 1
		}

		val totalFoundStartIndex = body.indexOf("\"total_found\":")
		val totalFoundEndIndex = body.indexOf(",", startIndex = totalFoundStartIndex)
		val totalFoundCount = body.substring(totalFoundStartIndex + "\"total_found\":".length, totalFoundEndIndex).toInt()
		val totalCountString = "\"total_count\":$totalFoundCount"

		val incompleteResultsString = "\"incomplete_results\":${(totalFoundCount > totalCount) && (currentPage == lastPage)}"

		val itemsString = if (totalFoundCount == 0) {
			"\"items\":[]"
		} else {
			val itemsStartIndex = body.indexOf("\"matches\":")
			val itemsEndIndex = body.indexOf("}]", startIndex = itemsStartIndex)
			val items = body.substring(itemsStartIndex + "\"matches\":".length, itemsEndIndex)
			"\"items\":$items}]"
		}

		val json = "{$lastString,$totalCountString,$incompleteResultsString,$itemsString}"
		return response.newBuilder().body(ResponseBody.create(response.body()!!.contentType(), json)).build()
	}

	private fun interceptResponses(request: Request, response: Response, isLastResponses: Boolean): Response {
		val body = response.body()!!.string()

		val totalCountStartIndex = body.indexOf("\"total_count\":")
		val totalCount = body.substring(totalCountStartIndex + "\"total_count\":\"".length, body.length - 2).toInt()

		val lastPage = (totalCount - 1) / RESPONSES_PER_PAGE
		val lastString = "\"last\":${lastPage + 1}"

		val incompleteResultsString = "\"incomplete_results\":false"

		/*var currentPage = request.url().queryParameter("page")?.toInt()
		if (currentPage == null) {
			currentPage = 1
		}

		val incompleteResults = if (isLastResponses) currentPage == LAST_RESPONSES_MAX_PAGE else false
		val incompleteResultsString = "\"incomplete_results\":$incompleteResults"*/

		val json = "{$lastString,$incompleteResultsString,${body.substring(1)}"
		return response.newBuilder().body(ResponseBody.create(response.body()!!.contentType(), json)).build()
	}
}