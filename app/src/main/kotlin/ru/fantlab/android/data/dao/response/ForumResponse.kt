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
			val forumBlock = DataManager.gson.fromJson(content, Forum::class.java)
			val array = forumBlock.topics
			array.map {
				topics.add(it)
			}

			val pagesBlock = forumBlock.pages
			val totalCount = pagesBlock.count

			val topics = Pageable(totalCount, pagesBlock.current, topics)

			return ForumResponse(topics)
		}
	}
}