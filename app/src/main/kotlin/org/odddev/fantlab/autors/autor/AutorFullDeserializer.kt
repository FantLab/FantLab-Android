package org.odddev.fantlab.autors.autor

import android.util.SparseArray
import android.util.SparseIntArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.odddev.fantlab.core.utils.*
import java.lang.reflect.Type
import java.util.*

class AutorFullDeserializer : JsonDeserializer<AutorFull> {

	fun JsonObject.parseAllWorks(works: SparseArray<List<AutorFull.Work>>) {
		for ((key, value) in this.entrySet()) {
			val worksList = ArrayList<AutorFull.Work>()
			value.asJsonObject.getAsJsonArray("list").parseBlockWorks(worksList)
			works.put(key.toInt(), worksList)
		}
	}

	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): AutorFull {
		val jsonObject = json.asJsonObject

		val awards = ArrayList<AutorFull.Award>()
		jsonObject.getAsJsonObject("awards")?.getAsJsonArray("nom")?.parseAwards(awards)
		jsonObject.getAsJsonObject("awards")?.getAsJsonArray("win")?.parseAwards(awards)

		val biography = AutorFull.Biography(
				anons = jsonObject.get("anons").getField()?.asString ?: "",
				text = jsonObject.get("biography").getField()?.asString ?: "",
				notes = jsonObject.get("biography_notes").getField()?.asString ?: "",
				source = jsonObject.get("source").getField()?.asString ?: "",
				sourceLink = jsonObject.get("source_link").getField()?.asString ?: "",
				birthday = jsonObject.get("birthday").getField()?.asString?.parseToDate(),
				deathday = jsonObject.get("deathday").getField()?.asString?.parseToDate(),
				name = jsonObject.get("name").getField()?.asString ?: "",
				nameOrig = jsonObject.get("name_orig").getField()?.asString ?: "",
				nameRp = jsonObject.get("name_rp").getField()?.asString ?: "",
				nameShort = jsonObject.get("name_short").getField()?.asString ?: "",
				sex = jsonObject.get("sex").getField()?.asString ?: ""
		)

		val country = AutorFull.Country(
				id = jsonObject.get("country_id").getField()?.asInt ?: -1,
				name = jsonObject.get("country_name").getField()?.asString ?: ""
		)

		val myMarks = SparseIntArray()
		for ((key, value) in jsonObject.getAsJsonObject("my")?.getAsJsonObject("marks")?.entrySet() ?: emptySet()) {
			myMarks.put(key.toInt(), value.asInt)
		}

		val myResponses = ArrayList<Int>()
		for ((key, _) in jsonObject.getAsJsonObject("my")?.getAsJsonObject("resps")?.entrySet() ?: emptySet()) {
			myResponses.add(key.toInt())
		}

		val sites = ArrayList<AutorFull.Site>()
		jsonObject.getAsJsonArray("sites")?.map { it.asJsonObject }?.mapTo(sites) {
			AutorFull.Site(
					description = it.get("descr").asString,
					site = it.get("site").asString
			)
		}

		val stat = jsonObject.getAsJsonObject("stat").parseStat()

		val works = SparseArray<List<AutorFull.Work>>()
		jsonObject.getAsJsonObject("cycles_blocks")?.parseAllWorks(works)
		jsonObject.getAsJsonObject("works_blocks")?.parseAllWorks(works)

		return AutorFull(
				id = jsonObject.get("autor_id").asInt,
				biography = biography,
				country = country,
				awards = awards,
				works = works,
				sites = sites,
				stat = stat,
				type = jsonObject.get("type").getField()?.asString ?: "",
				compiler = jsonObject.get("compiler").getField()?.asString ?: "",
				curator = jsonObject.get("curator").getField()?.asInt ?: -1,
				fantastic = jsonObject.get("fantastic").getField()?.asInt ?: -1,
				isFv = jsonObject.get("is_fv").getField()?.asInt == 1,
				isOpened = jsonObject.get("is_opened").getField()?.asInt == 1,
				myMarks = myMarks,
				myResponses = myResponses
		)
	}
}