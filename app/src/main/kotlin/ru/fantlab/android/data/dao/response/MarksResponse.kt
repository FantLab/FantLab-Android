package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.Mark
import ru.fantlab.android.data.dao.model.MarksStatistics
import ru.fantlab.android.provider.rest.DataManager

data class MarksResponse(
		val marks: Pageable<Mark>,
		val marksStatistics: MarksStatistics
) {
	class Deserializer : ResponseDeserializable<MarksResponse> {

		override fun deserialize(content: String): MarksResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val items: ArrayList<Mark> = arrayListOf()
			val array = jsonObject.getAsJsonArray("items")
			array.map {
				items.add(DataManager.gson.fromJson(it, Mark::class.java))
			}
			val totalCount = jsonObject.getAsJsonPrimitive("total_count").asInt
			val lastPage = (totalCount - 1) / PER_PAGE + 1
			val marks = Pageable(lastPage, totalCount, false, items)
			val `object` = jsonObject.getAsJsonObject("user_stat")
			val marksStatistics = DataManager.gson.fromJson(`object`, MarksStatistics::class.java)
			return MarksResponse(marks, marksStatistics)
		}

		companion object {
			private const val PER_PAGE = 200
		}
	}
}