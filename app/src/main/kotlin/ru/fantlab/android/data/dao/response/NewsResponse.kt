package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.News
import ru.fantlab.android.provider.rest.DataManager

data class NewsResponse(
		val news: Pageable<News>
) {
	class Deserializer(private val perPage: Int) : ResponseDeserializable<NewsResponse> {

		override fun deserialize(content: String): NewsResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val items: ArrayList<News> = arrayListOf()
			val array = jsonObject.getAsJsonArray("items")
			array.map {
				items.add(DataManager.gson.fromJson(it, News::class.java))
			}
			val totalCount = jsonObject.getAsJsonPrimitive("total_count").asInt
			val lastPage = (totalCount - 1) / perPage + 1
			val news = Pageable(lastPage, totalCount, items)
			return NewsResponse(news)
		}
	}
}