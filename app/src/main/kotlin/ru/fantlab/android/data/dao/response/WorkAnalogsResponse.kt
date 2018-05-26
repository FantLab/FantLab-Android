package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.WorkAnalog
import ru.fantlab.android.provider.rest.DataManager

data class WorkAnalogsResponse(
		val analogs: ArrayList<WorkAnalog>
) {
	class Deserializer : ResponseDeserializable<WorkAnalogsResponse> {

		private val analogs: ArrayList<WorkAnalog> = arrayListOf()

		override fun deserialize(content: String): WorkAnalogsResponse {
			val jsonArray = JsonParser().parse(content).asJsonArray
			jsonArray.map {
				analogs.add(DataManager.gson.fromJson(it, WorkAnalog::class.java))
			}
			return WorkAnalogsResponse(analogs)
		}
	}
}