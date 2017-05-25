package org.odddev.fantlab.autors.autor

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.odddev.fantlab.core.utils.parseToDate
import java.lang.reflect.Type

/**
 * Created by kefir on 28.01.2017.
 */
class AutorFullDeserializer : JsonDeserializer<AutorFull> {

	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): AutorFull {
		val jsonObject = json.asJsonObject

		val sitesArray = jsonObject.getAsJsonArray("sites")
		val sites = ArrayList<AutorFull.Site>(sitesArray.size())
		sitesArray.mapTo(sites) {
			AutorFull.Site(
					it.asJsonObject.get("descr").asString,
					it.asJsonObject.get("site").asString
			)
		}

		val statObject = jsonObject.get("stat").asJsonObject
		val stat = AutorFull.Stat(
				statObject.get("awardcount").asInt,
				statObject.get("editioncount").asString.toInt(),
				statObject.get("moviecount").asString.toInt(),
				statObject.get("markcount").asString.toInt(),
				statObject.get("responsecount").asString.toInt()
		)

		return AutorFull(
				anons = jsonObject.get("anons").asString ?: "",
				autorId = jsonObject.get("autor_id").asString.toInt(),
				biography = jsonObject.get("biography").asString ?: "",
				biographyNotes = jsonObject.get("biography_notes").asString ?: "",
				birthday = jsonObject.get("birthday").asString.parseToDate(),
				compiler = jsonObject.get("compiler").asString ?: "",
				countryId = jsonObject.get("country_id").asString.toInt(),
				countryName = jsonObject.get("country_name").asString ?: "",
				curator = jsonObject.get("curator").asString.toInt(),
				fantastic = jsonObject.get("fantastic").asString.toInt(),
				isFv = jsonObject.get("is_fv").asString.toInt() == 1,
				isOpened = jsonObject.get("is_opened").asString.toInt() == 1,
				name = jsonObject.get("name").asString,
				nameOrig = jsonObject.get("name_orig")?.asString ?: "",
				nameRp = jsonObject.get("name_rp").asString,
				nameShort = jsonObject.get("name_short").asString,
				sex = jsonObject.get("sex").asString ?: "",
				sites = sites,
				source = jsonObject.get("source").asString ?: "",
				sourceLink = jsonObject.get("source_link").asString ?: "",
				stat = stat,
				type = jsonObject.get("type").asString
		)
	}
}