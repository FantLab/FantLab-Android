package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.model.TopicMessageDraft
import ru.fantlab.android.provider.rest.DataManager

data class TopicMessageDraftResponse(
		val message: TopicMessageDraft
) {
	class Deserializer : ResponseDeserializable<TopicMessageDraftResponse> {
		override fun deserialize(content: String): TopicMessageDraftResponse {
			println(content)
			val message = DataManager.gson.fromJson(content, TopicMessageDraft::class.java)
			println(message)
			return TopicMessageDraftResponse(message)
		}
	}
}