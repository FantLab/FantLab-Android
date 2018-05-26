package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.SearchEdition
import ru.fantlab.android.provider.rest.DataManager

data class SearchEditionsResponse(
		val editions: Pageable<SearchEdition>
) {
	class Deserializer(private val perPage: Int) : ResponseDeserializable<SearchEditionsResponse> {

		override fun deserialize(content: String): SearchEditionsResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val items: ArrayList<SearchEdition> = arrayListOf()
			val array = jsonObject.getAsJsonArray("matches")
			array.map {
				items.add(DataManager.gson.fromJson(it, SearchEdition::class.java))
			}
			val totalCount = jsonObject.getAsJsonPrimitive("total_found").asInt
			val lastPage = (totalCount - 1) / perPage + 1
			val editions = Pageable(lastPage, totalCount, items)
			return SearchEditionsResponse(editions)
		}
	}
}