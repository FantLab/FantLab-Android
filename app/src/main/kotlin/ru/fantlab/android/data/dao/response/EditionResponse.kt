package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonNull
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.AdditionalImages
import ru.fantlab.android.data.dao.model.Edition
import ru.fantlab.android.provider.rest.DataManager

data class EditionResponse(
		val edition: Edition,
		val editionContent: ArrayList<String>?,
		val additionalImages: AdditionalImages?
) {
	class Deserializer : ResponseDeserializable<EditionResponse> {

		private lateinit var edition: Edition
		private val editionContent: ArrayList<String> = arrayListOf()
		private var additionalImages: AdditionalImages? = null

		override fun deserialize(content: String): EditionResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			edition = DataManager.gson.fromJson(jsonObject, Edition::class.java)
			if (jsonObject["content"] != JsonNull.INSTANCE) {
				val array = jsonObject.getAsJsonArray("content")
				array.map {
					editionContent.add(it.asJsonPrimitive.asString)
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
	}
}