package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.newmodel.AuthorInList
import ru.fantlab.android.provider.rest.DataManager

data class AuthorsResponse(
		val authors: ArrayList<AuthorInList>
) {
	class Deserializer : ResponseDeserializable<AuthorsResponse> {

		private val authors: ArrayList<AuthorInList> = arrayListOf()

		override fun deserialize(content: String): AuthorsResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			val array = jsonObject.getAsJsonArray("list")
			array.map {
				authors.add(DataManager.gson.fromJson(it, AuthorInList::class.java))
			}
			return AuthorsResponse(authors)
		}
	}
}