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
			val forumBlock = JsonParser().parse(content).asJsonObject
			val array = forumBlock.getAsJsonArray("forum_blocks")
			array.map {
				forums.add(DataManager.gson.fromJson(it, Forums.ForumBlock::class.java))
			}

			return ForumsResponse(forums)
		}
	}
}