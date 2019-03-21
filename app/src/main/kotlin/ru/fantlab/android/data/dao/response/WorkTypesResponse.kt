package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.WorkType
import ru.fantlab.android.provider.rest.DataManager
import java.util.*

data class WorkTypesResponse(
		val types: ArrayList<WorkType>
) {
	class Deserializer : ResponseDeserializable<WorkTypesResponse> {

		override fun deserialize(content: String): WorkTypesResponse {
			val items: ArrayList<WorkType> = arrayListOf()
			val array = JsonParser().parse(content).asJsonArray
			array.map {
				items.add(DataManager.gson.fromJson(it, WorkType::class.java))
			}
			return WorkTypesResponse(items)
		}
	}
}