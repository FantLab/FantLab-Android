package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.provider.rest.DataManager

data class AwardResponse(
		val award: Award
) {
	class Deserializer : ResponseDeserializable<AwardResponse> {
		private lateinit var award: Award

		override fun deserialize(content: String): AwardResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			award = DataManager.gson.fromJson(jsonObject, Award::class.java)
			return AwardResponse(award)
		}
	}
}