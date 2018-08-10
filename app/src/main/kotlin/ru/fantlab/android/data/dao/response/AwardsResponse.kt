package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.AwardInList
import ru.fantlab.android.provider.rest.DataManager

data class AwardsResponse(
		val awards: ArrayList<AwardInList>
) {
	class Deserializer : ResponseDeserializable<AwardsResponse> {

		private val awards: ArrayList<AwardInList> = arrayListOf()

		override fun deserialize(content: String): AwardsResponse {
			val array = JsonParser().parse(content).asJsonArray
			array.map {
				awards.add(DataManager.gson.fromJson(it, AwardInList::class.java))
			}

			return AwardsResponse(awards)
		}
	}
}