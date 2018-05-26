package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.SearchAward
import ru.fantlab.android.provider.rest.DataManager

data class SearchAwardsResponse(
		val awards: Pageable<SearchAward>
) {
	class Deserializer(private val perPage: Int) : ResponseDeserializable<SearchAwardsResponse> {

		override fun deserialize(content: String): SearchAwardsResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val items: ArrayList<SearchAward> = arrayListOf()
			val array = jsonObject.getAsJsonArray("matches")
			array.map {
				items.add(DataManager.gson.fromJson(it, SearchAward::class.java))
			}
			val totalCount = jsonObject.getAsJsonPrimitive("total_found").asInt
			val lastPage = (totalCount - 1) / perPage + 1
			val awards = Pageable(lastPage, totalCount, false, items)
			return SearchAwardsResponse(awards)
		}
	}
}