package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.AwardInList
import ru.fantlab.android.data.dao.model.Forums
import ru.fantlab.android.provider.rest.DataManager

data class ForumsResponse(
		val forums: ArrayList<Forums.ForumBlock>
) {
	class Deserializer : ResponseDeserializable<ForumsResponse> {

		private val forums: ArrayList<Forums.ForumBlock> = arrayListOf()

		override fun deserialize(content: String): ForumsResponse {
			val forums = DataManager.gson.fromJson(content, Forums::class.java)
			return ForumsResponse(forums.forumBlocks)
		}
	}
}