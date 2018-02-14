package ru.fantlab.android.provider.rest.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody


class PaginationInterceptor : Interceptor {

	companion object {

		private val SEARCH_RESULTS_PER_PAGE = 25
	}

	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val response = chain.proceed(request)
		if (response.isSuccessful) {
			request.url().pathSegments().map {
				if (it.contains("search-")) { // search
					return interceptSearch(request, response)
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
		val nextString = if (currentPage <= lastPage) {
			"\"next\":${currentPage + 1}"
		} else {
			"\"next\":0"
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

		val json = "{$nextString,$lastString,$totalCountString,$incompleteResultsString,$itemsString}"
		return response.newBuilder().body(ResponseBody.create(response.body()!!.contentType(), json)).build()
	}
}