package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser

data class MarkResponse(
		val markCount: Double,
		val midMark: Double
) {
	class Deserializer() : ResponseDeserializable<MarkResponse> {

		override fun deserialize(content: String): MarkResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val markCount = jsonObject.getAsJsonPrimitive("markcount").asDouble
			val midMark = jsonObject.getAsJsonPrimitive("midmark").asDouble

			return MarkResponse(markCount, midMark)
		}
	}
}