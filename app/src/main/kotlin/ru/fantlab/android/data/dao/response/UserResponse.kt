package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.model.User
import ru.fantlab.android.provider.rest.DataManager

data class UserResponse(
		val user: User
) {
	class Deserializer : ResponseDeserializable<UserResponse> {

		override fun deserialize(content: String): UserResponse {
			val user = DataManager.gson.fromJson(content, User::class.java)
			return UserResponse(user)
		}
	}
}