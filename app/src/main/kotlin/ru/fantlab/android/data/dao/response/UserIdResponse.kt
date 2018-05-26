package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.newmodel.UserId
import ru.fantlab.android.provider.rest.DataManager

data class UserIdResponse(
		val userId: UserId
) {
	class Deserializer : ResponseDeserializable<UserIdResponse> {

		override fun deserialize(content: String): UserIdResponse {
			val userId = DataManager.gson.fromJson(content, UserId::class.java)
			return UserIdResponse(userId)
		}
	}
}