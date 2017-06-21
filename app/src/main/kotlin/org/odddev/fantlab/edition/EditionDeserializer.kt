package org.odddev.fantlab.edition

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.odddev.fantlab.core.utils.getField
import java.lang.reflect.Type

class EditionDeserializer : JsonDeserializer<Edition> {

	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Edition {
		val jsonObject = json.asJsonObject

		return Edition(
				content = ArrayList(),
				copies = jsonObject.get("copies").getField()?.asInt ?: -1,
				correctLevel = jsonObject.get("correct_level").asFloat,
				covertType = jsonObject.get("covertype").getField()?.asString ?: "",
				description = jsonObject.get("description").getField()?.asString ?: "",
				id = jsonObject.get("edition_id").asInt,
				types = ArrayList(),
				format = "",
				images = ArrayList(),
				covers = ArrayList(),
				isbns = ArrayList(),
				lang = Edition.Language("", ""),
				notes = jsonObject.get("notes").asString,
				origName = jsonObject.get("origname").asString,
				pages = jsonObject.get("pages").getField()?.asInt ?: -1,
				plan = Edition.Plan("", ""),
				series = jsonObject.get("series").getField()?.asString ?: "",
				type = jsonObject.get("type").asInt,
				year = jsonObject.get("year").getField()?.asInt ?: -1
		)
	}
}