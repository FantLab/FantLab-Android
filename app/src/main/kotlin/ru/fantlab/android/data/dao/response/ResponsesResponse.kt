package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.newmodel.Response
import ru.fantlab.android.provider.rest.DataManager

data class ResponsesResponse(
		val responses: Pageable<Response>
) {
	class Deserializer : ResponseDeserializable<ResponsesResponse> {

		override fun deserialize(content: String): ResponsesResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val items: ArrayList<Response> = arrayListOf()
			val array = jsonObject.getAsJsonArray("items")
			array.map {
				items.add(DataManager.gson.fromJson(it, Response::class.java))
			}
			val totalCount = jsonObject.getAsJsonPrimitive("total_count").asInt
			val lastPage = (totalCount - 1) / RESPONSES_PER_PAGE + 1
			val responses = Pageable(lastPage, totalCount, false, items)
			return ResponsesResponse(responses)
		}
	}

	companion object {
		private const val RESPONSES_PER_PAGE = 50
	}
}