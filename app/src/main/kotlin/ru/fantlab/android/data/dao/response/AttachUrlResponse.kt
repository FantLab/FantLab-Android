package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.model.AttachUrl
import ru.fantlab.android.provider.rest.DataManager

data class AttachUrlResponse(val url: String = "") {

	class Deserializer : ResponseDeserializable<AttachUrl> {
		override fun deserialize(content: String): AttachUrl = DataManager.gson.fromJson(content, AttachUrl::class.java)
	}

}