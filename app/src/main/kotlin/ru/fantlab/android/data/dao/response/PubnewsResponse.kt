package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.Pubnews
import ru.fantlab.android.provider.rest.DataManager

data class PubnewsResponse(
		val editions: Pageable<Pubnews.Object>,
		val publisherList: List<Pubnews.Publisher>
) {
	class Deserializer : ResponseDeserializable<PubnewsResponse> {


		override fun deserialize(content: String): PubnewsResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val items: ArrayList<Pubnews.Object> = arrayListOf()
			val publishers: ArrayList<Pubnews.Publisher> = arrayListOf()

			val array = jsonObject.getAsJsonArray("objects")
			array.map {
				items.add(DataManager.gson.fromJson(it, Pubnews.Object::class.java))
			}

			val publishersArray = jsonObject.getAsJsonArray("publisher_list")
			publishersArray.map {
				publishers.add(DataManager.gson.fromJson(it, Pubnews.Publisher::class.java))
			}

			val totalCount = jsonObject.getAsJsonPrimitive("total_count").asInt
			val lastPage = jsonObject.getAsJsonPrimitive("page_count").asInt
			val responses = Pageable(lastPage, totalCount, items)
			return PubnewsResponse(responses, publishers)
		}
	}
}