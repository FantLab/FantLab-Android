package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.newmodel.SearchWork
import ru.fantlab.android.provider.rest.DataManager

data class SearchWorksResponse(
		val works: Pageable<SearchWork>
) {
	class Deserializer(private val perPage: Int) : ResponseDeserializable<SearchWorksResponse> {

		override fun deserialize(content: String): SearchWorksResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val items: ArrayList<SearchWork> = arrayListOf()
			val array = jsonObject.getAsJsonArray("matches")
			array.map {
				items.add(DataManager.gson.fromJson(it, SearchWork::class.java))
			}
			val totalCount = jsonObject.getAsJsonPrimitive("total_found").asInt
			val lastPage = (totalCount - 1) / perPage + 1
			val works = Pageable(lastPage, totalCount, false, items)
			return SearchWorksResponse(works)
		}
	}
}