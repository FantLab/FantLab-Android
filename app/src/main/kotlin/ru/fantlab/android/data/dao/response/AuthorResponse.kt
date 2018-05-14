package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.newmodel.Author
import ru.fantlab.android.provider.rest.DataManager

data class AuthorResponse(
		val author: Author
) {
	class Deserializer : ResponseDeserializable<AuthorResponse> {

		private lateinit var author: Author

		override fun deserialize(content: String): AuthorResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			author = DataManager.gson.fromJson(jsonObject, Author::class.java)
			return AuthorResponse(
					author
			)
		}
	}
}