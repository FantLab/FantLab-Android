package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser

data class MarkResponse(
		val markCount: String,
		val midMark: String
) {
	class Deserializer() : ResponseDeserializable<MarkResponse> {

		override fun deserialize(content: String): MarkResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val markCount = jsonObject.getAsJsonPrimitive("markcount").asString
			val midMark = jsonObject.getAsJsonPrimitive("midmark").asString

			return MarkResponse(markCount, midMark)
		}
	}
}