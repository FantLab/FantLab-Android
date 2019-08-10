package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.model.Community
import ru.fantlab.android.provider.rest.DataManager

data class CommunitiesResponse(
		val communitiesMain: ArrayList<Community.Main>,
		val communitiesAdditional: ArrayList<Community.Additional>
) {
	class Deserializer : ResponseDeserializable<CommunitiesResponse> {

		private val communitiesMain: ArrayList<Community.Main> = arrayListOf()
		private val communitiesAdditional: ArrayList<Community.Additional> = arrayListOf()

		override fun deserialize(content: String): CommunitiesResponse {
			val forumBlock = DataManager.gson.fromJson(content, Community::class.java)

			forumBlock.main.map {
				communitiesMain.add(it)
			}

			forumBlock.additional.map {
				communitiesAdditional.add(it)
			}

			return CommunitiesResponse(communitiesMain, communitiesAdditional)
		}
	}
}