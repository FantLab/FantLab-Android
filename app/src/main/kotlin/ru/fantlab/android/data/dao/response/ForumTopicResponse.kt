package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.Forum
import ru.fantlab.android.data.dao.model.ForumTopic
import ru.fantlab.android.provider.rest.DataManager
import ru.fantlab.android.provider.rest.isNullable

data class ForumTopicResponse(
		val topic: ForumTopic.Topic,
		val forum: ForumTopic.Forum,
		val pinnedMessage: ForumTopic.PinnedMessage?,
		val messages: Pageable<ForumTopic.Message>
) {
	class Deserializer : ResponseDeserializable<ForumTopicResponse> {

		private val messages: ArrayList<ForumTopic.Message> = arrayListOf()
		private lateinit var topic: ForumTopic.Topic
		private lateinit var forum: ForumTopic.Forum
		private var pinnedMessage: ForumTopic.PinnedMessage? = null

		override fun deserialize(content: String): ForumTopicResponse {
			val topicBlock = JsonParser().parse(content).asJsonObject

			if (!topicBlock["topic"].isNullable()) {
				val `object` = topicBlock.getAsJsonObject("topic")
				topic = DataManager.gson.fromJson(`object`, ForumTopic.Topic::class.java)
			}

			if (!topicBlock["forum"].isNullable()) {
				val `object` = topicBlock.getAsJsonObject("forum")
				forum = DataManager.gson.fromJson(`object`, ForumTopic.Forum::class.java)
			}

			if (!topicBlock["pinnedMessage"].isNullable()) {
				val `object` = topicBlock.getAsJsonObject("pinnedMessage")
				pinnedMessage = DataManager.gson.fromJson(`object`, ForumTopic.PinnedMessage::class.java)
			}

			val array = topicBlock.getAsJsonArray("messages")
			array.map {
				messages.add(DataManager.gson.fromJson(it, ForumTopic.Message::class.java))
			}

			val `object` = topicBlock.getAsJsonObject("pages")
			val pagesBlock = DataManager.gson.fromJson(`object`, Forum.Pages::class.java)
			val totalCount = pagesBlock.count

			val messages = Pageable(totalCount, pagesBlock.current, messages)

			return ForumTopicResponse(topic, forum, pinnedMessage, messages)
		}
	}
}