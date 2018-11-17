package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonNull
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.AdditionalImages
import ru.fantlab.android.data.dao.model.Edition
import ru.fantlab.android.data.dao.model.EditionContent
import ru.fantlab.android.provider.rest.DataManager

data class EditionResponse(
		val edition: Edition,
		val editionContent: ArrayList<EditionContent>,
		val additionalImages: AdditionalImages?
) {
	class Deserializer : ResponseDeserializable<EditionResponse> {

		private lateinit var edition: Edition
		private val editionContent: ArrayList<EditionContent> = arrayListOf()
		private var additionalImages: AdditionalImages? = null

		override fun deserialize(content: String): EditionResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			edition = DataManager.gson.fromJson(jsonObject, Edition::class.java)
			if (jsonObject["content"] != JsonNull.INSTANCE) {
				val array = jsonObject.getAsJsonArray("content")
				array.map { it ->
					val title = it.asJsonPrimitive.asString
					val level = getLevel(title.toCharArray())
					editionContent.add(EditionContent(title.trim(), level))
				}
			}
			if (jsonObject["images_plus"] != JsonNull.INSTANCE) {
				val `object` = jsonObject.getAsJsonObject("images_plus")
				additionalImages = DataManager.gson.fromJson(`object`, AdditionalImages::class.java)
			}
			return EditionResponse(
					edition,
					editionContent,
					additionalImages
			)
		}

		private fun getLevel(chars: CharArray): Int {
			var spaceCount = 0
			chars.forEach {
				if (it == ' ') {
					spaceCount++
				} else return spaceCount
			}
			return spaceCount
		}

	}
}