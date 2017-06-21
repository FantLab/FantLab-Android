package org.odddev.fantlab.award

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.odddev.fantlab.core.utils.parseToDate
import java.lang.reflect.Type

class AwardDeserializer : JsonDeserializer<Award> {

	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): Award {
		val jsonObject = json.asJsonObject

		return Award(
				awardId = jsonObject.get("award_id").asString,
				country = jsonObject.get("country_name")?.asString ?: "",
				description = jsonObject.get("description")?.asString ?: "",
				maxDate = jsonObject.get("max_date").asString.parseToDate(),
				minDate = jsonObject.get("min_date").asString.parseToDate(),
				name = jsonObject.get("name")?.asString ?: "",
				rusName = jsonObject.get("rusname")?.asString ?: "",
				language = jsonObject.get("lang_id")?.asInt ?: 0,
				type = jsonObject.get("award_type")?.asInt ?: 0
		)
	}
}