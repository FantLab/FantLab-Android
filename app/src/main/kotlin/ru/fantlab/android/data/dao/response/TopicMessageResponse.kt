package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.model.TopicMessage
import ru.fantlab.android.provider.rest.DataManager

data class TopicMessageResponse(
		val message: TopicMessage
) {
	class Deserializer : ResponseDeserializable<TopicMessageResponse> {
		override fun deserialize(content: String): TopicMessageResponse {
			val message = DataManager.gson.fromJson(content, TopicMessage::class.java)
			return TopicMessageResponse(message)
		}
	}
}