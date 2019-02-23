package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.Pubplans
import ru.fantlab.android.provider.rest.DataManager

data class PubplansResponse(
		val editions: Pageable<Pubplans.Object>,
		val publisherList: List<Pubplans.Publisher>
) {
	class Deserializer : ResponseDeserializable<PubplansResponse> {


		override fun deserialize(content: String): PubplansResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val items: ArrayList<Pubplans.Object> = arrayListOf()
			val publishers: ArrayList<Pubplans.Publisher> = arrayListOf()

			val array = jsonObject.getAsJsonArray("objects")
			array.map {
				items.add(DataManager.gson.fromJson(it, Pubplans.Object::class.java))
			}

			val publishersArray = jsonObject.getAsJsonArray("publisher_list")
			publishersArray.map {
				publishers.add(DataManager.gson.fromJson(it, Pubplans.Publisher::class.java))
			}

			val totalCount = jsonObject.getAsJsonPrimitive("total_count").asInt
			val lastPage = jsonObject.getAsJsonPrimitive("page_count").asInt
			val responses = Pageable(lastPage, totalCount, items)
			return PubplansResponse(responses, publishers)
		}
	}
}