package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.Autplans
import ru.fantlab.android.provider.rest.DataManager

data class AutplansResponse(
		val plans: Pageable<Autplans.Object>
) {
	class Deserializer : ResponseDeserializable<AutplansResponse> {


		override fun deserialize(content: String): AutplansResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val items: ArrayList<Autplans.Object> = arrayListOf()

			val array = jsonObject.getAsJsonArray("objects")
			array.map {
				items.add(DataManager.gson.fromJson(it, Autplans.Object::class.java))
			}

			val totalCount = jsonObject.getAsJsonPrimitive("total_count").asInt
			val lastPage = jsonObject.getAsJsonPrimitive("page_count").asInt
			val responses = Pageable(lastPage, totalCount, items)
			return AutplansResponse(responses)
		}
	}
}