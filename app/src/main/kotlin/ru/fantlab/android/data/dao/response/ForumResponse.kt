package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.AwardInList
import ru.fantlab.android.data.dao.model.Forum
import ru.fantlab.android.data.dao.model.Forums
import ru.fantlab.android.provider.rest.DataManager

data class ForumResponse(
		val topics: Pageable<Forum.Topic>
) {
	class Deserializer : ResponseDeserializable<ForumResponse> {

		private val topics: ArrayList<Forum.Topic> = arrayListOf()

		override fun deserialize(content: String): ForumResponse {
			val forumBlock = JsonParser().parse(content).asJsonObject
			val array = forumBlock.getAsJsonArray("topics")
			array.map {
				topics.add(DataManager.gson.fromJson(it, Forum.Topic::class.java))
			}

			val `object` = forumBlock.getAsJsonObject("pages")
			val pagesBlock = DataManager.gson.fromJson(`object`, Forum.Pages::class.java)
			val totalCount = pagesBlock.count

			val topics = Pageable(array.size(), array.size() * totalCount, topics)

			return ForumResponse(topics)
		}
	}
}