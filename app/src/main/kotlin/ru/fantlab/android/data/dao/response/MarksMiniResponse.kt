package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.MarkMini
import ru.fantlab.android.provider.rest.DataManager

data class MarksMiniResponse(
		val marks: ArrayList<MarkMini>,
		val marksCount: Int
) {
	class Deserializer : ResponseDeserializable<MarksMiniResponse> {
		override fun deserialize(content: String): MarksMiniResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val items: ArrayList<MarkMini> = arrayListOf()
			jsonObject.getAsJsonArray("items")?.map {
				items.add(DataManager.gson.fromJson(it, MarkMini::class.java))
			}
			val marksCount = jsonObject.getAsJsonPrimitive("total_count").asInt
			return MarksMiniResponse(items, marksCount)
		}
	}
}