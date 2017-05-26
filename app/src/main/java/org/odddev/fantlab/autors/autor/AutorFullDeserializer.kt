package org.odddev.fantlab.autors.autor

import android.util.SparseArray
import android.util.SparseIntArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.odddev.fantlab.core.utils.parseToDate
import java.lang.reflect.Type
import java.util.*

/**
 * Created by kefir on 28.01.2017.
 */
class AutorFullDeserializer : JsonDeserializer<AutorFull> {

	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): AutorFull {
		val jsonObject = json.asJsonObject

		val awardsObject = jsonObject.getAsJsonObject("awards")
		val nom = awardsObject.getAsJsonArray("nom")
		val win = awardsObject.getAsJsonArray("win")
		val awards = ArrayList<AutorFull.Award>(nom.size() + win.size())

		nom.mapTo(awards) {
			AutorFull.Award(
					it.asJsonObject.get("award_id").asString.toInt(),
					it.asJsonObject.get("award_in_list").asString.toInt() == 1,
					it.asJsonObject.get("award_is_opened").asString.toInt() == 1,
					it.asJsonObject.get("award_name").asString,
					it.asJsonObject.get("award_rusname").asString,
					it.asJsonObject.get("contest_id").asString.toInt(),
					it.asJsonObject.get("contest_name").asString,
					it.asJsonObject.get("contest_year").asString.toInt(),
					it.asJsonObject.get("cw_id").asString.toInt(),
					it.asJsonObject.get("cw_is_winner").asString.toInt() == 1,
					it.asJsonObject.get("cw_postfix").asString,
					it.asJsonObject.get("cw_prefix").asString,
					it.asJsonObject.get("nomination_id")?.asString?.toInt() ?: -1,
					it.asJsonObject.get("nomination_name")?.asString ?: "",
					it.asJsonObject.get("nomination_rusname")?.asString ?: "",
					it.asJsonObject.get("work_id").asString.toInt(),
					it.asJsonObject.get("work_name").asString,
					it.asJsonObject.get("work_rusname").asString,
					it.asJsonObject.get("work_year").asString.toInt()
			)
		}
		win.mapTo(awards) {
			AutorFull.Award(
					it.asJsonObject.get("award_id").asString.toInt(),
					it.asJsonObject.get("award_in_list").asString.toInt() == 1,
					it.asJsonObject.get("award_is_opened").asString.toInt() == 1,
					it.asJsonObject.get("award_name").asString,
					it.asJsonObject.get("award_rusname").asString,
					it.asJsonObject.get("contest_id").asString.toInt(),
					it.asJsonObject.get("contest_name").asString,
					it.asJsonObject.get("contest_year").asString.toInt(),
					it.asJsonObject.get("cw_id").asString.toInt(),
					it.asJsonObject.get("cw_is_winner").asString.toInt() == 1,
					it.asJsonObject.get("cw_postfix").asString,
					it.asJsonObject.get("cw_prefix").asString,
					it.asJsonObject.get("nomination_id")?.asString?.toInt() ?: -1,
					it.asJsonObject.get("nomination_name")?.asString ?: "",
					it.asJsonObject.get("nomination_rusname")?.asString ?: "",
					it.asJsonObject.get("work_id").asString.toInt(),
					it.asJsonObject.get("work_name").asString,
					it.asJsonObject.get("work_rusname").asString,
					it.asJsonObject.get("work_year").asString.toInt()
			)
		}

		val my = jsonObject.getAsJsonObject("my")
		val myMarks = SparseIntArray()
		for ((key, value) in my.getAsJsonObject("marks").entrySet()) {
			myMarks.put(key.toInt(), value.asString.toInt())
		}

		val myResponses = ArrayList<Int>()
		for ((key, _) in my.getAsJsonObject("resps").entrySet()) {
			myResponses.add(key.toInt())
		}

		val sitesList = jsonObject.getAsJsonArray("sites")
		val sites = ArrayList<AutorFull.Site>(sitesList.size())
		sitesList.mapTo(sites) {
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
				awards = awards,
				id = jsonObject.get("autor_id").asString.toInt(),
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
				myMarks = myMarks,
				myResponses = myResponses,
				name = jsonObject.get("name").asString,
				nameOrig = jsonObject.get("name_orig")?.asString ?: "",
				nameRp = jsonObject.get("name_rp").asString,
				nameShort = jsonObject.get("name_short").asString,
				sex = jsonObject.get("sex").asString ?: "",
				sites = sites,
				source = jsonObject.get("source").asString ?: "",
				sourceLink = jsonObject.get("source_link").asString ?: "",
				stat = stat,
				type = jsonObject.get("type").asString,
				works = SparseArray()
		)
	}
}