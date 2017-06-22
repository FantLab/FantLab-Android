package org.odddev.fantlab.work

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WorkDeserializer : JsonDeserializer<Work> {

	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Work {
		val jsonObject = json.asJsonObject

		return Work(
				id = jsonObject.get("work_id").asInt,
				name = jsonObject.get("work_name").asString,
				nameBonus = jsonObject.get("work_name_bonus").asString,
				nameOrig = jsonObject.get("work_name_orig").asString,
				notes = jsonObject.get("work_notes").asString,
				notFinished = jsonObject.get("work_notfinished").asInt == 1,
				parent = jsonObject.get("work_parent").asInt == 1,
				preparing = jsonObject.get("work_preparing").asInt == 1,
				type = jsonObject.get("work_type_id").asInt,
				year = jsonObject.get("work_year").asInt,
				canDownload = jsonObject.get("public_download_file").asInt == 1
		)
	}
}