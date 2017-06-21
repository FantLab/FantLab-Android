package org.odddev.fantlab.work

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WorkDeserializer : JsonDeserializer<Work> {

	override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Work {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}