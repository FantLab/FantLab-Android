package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.model.Login
import ru.fantlab.android.provider.rest.DataManager

data class LoginResponse(
		val login: Login
) {
	class Deserializer : ResponseDeserializable<LoginResponse> {

		override fun deserialize(content: String): LoginResponse {
			val login = DataManager.gson.fromJson(content, Login::class.java)
			return LoginResponse(login)
		}
	}
}