package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonNull
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.newmodel.EditionsBlocks
import ru.fantlab.android.data.dao.newmodel.EditionsInfo
import ru.fantlab.android.data.dao.newmodel.Statistics
import ru.fantlab.android.provider.rest.DataManager

data class AuthorEditionsResponse(
		val editions: EditionsBlocks?,
		val editionsInfo: EditionsInfo,
		val statistics: Statistics
) {
	class Deserializer : ResponseDeserializable<AuthorEditionsResponse> {

		override fun deserialize(content: String): AuthorEditionsResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			var editions: EditionsBlocks? = null
			if (jsonObject["editions_blocks"] != JsonNull.INSTANCE) {
				val `object` = jsonObject.getAsJsonObject("editions_blocks")
				editions = EditionsBlocks.Deserializer().deserialize(`object`.toString())
			}
			var `object` = jsonObject.getAsJsonObject("editions_info")
			val editionsInfo = EditionsInfo.Deserializer().deserialize(`object`.toString())
			`object` = jsonObject.getAsJsonObject("stat")
			val statistics: Statistics = DataManager.gson.fromJson(`object`, Statistics::class.java)
			return AuthorEditionsResponse(editions, editionsInfo, statistics)
		}
	}
}