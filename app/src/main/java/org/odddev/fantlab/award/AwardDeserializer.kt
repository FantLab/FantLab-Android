package org.odddev.fantlab.award

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.odddev.fantlab.core.utils.parseToYear
import java.lang.reflect.Type

/**
 * Created by kefir on 28.01.2017.
 */
class AwardDeserializer : JsonDeserializer<Award> {
    private val TAG = "AwardDeserializer"

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): Award {
        val jsonObject = json.asJsonObject

        return Award(
                awardId = jsonObject.get("award_id").asString,
                countryName = jsonObject.get("country_name")?.asString ?: "",
                description = jsonObject.get("description")?.asString ?: "",
                maxDate = jsonObject.get("max_date").asString.parseToYear(),
                minDate = jsonObject.get("min_date").asString.parseToYear(),
                name = jsonObject.get("name")?.asString ?: "",
                rusName = jsonObject.get("rus_name")?.asString ?: ""
        )
    }
}