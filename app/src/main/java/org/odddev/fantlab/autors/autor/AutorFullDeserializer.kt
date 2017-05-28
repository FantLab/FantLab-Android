package org.odddev.fantlab.autors.autor

import android.util.SparseArray
import android.util.SparseIntArray
import com.google.gson.*
import org.odddev.fantlab.core.utils.getField
import org.odddev.fantlab.core.utils.parseToDate
import java.lang.reflect.Type
import java.util.*

/**
 * Created by kefir on 28.01.2017.
 */
class AutorFullDeserializer : JsonDeserializer<AutorFull> {

	fun JsonArray.parseAwards(awards: ArrayList<AutorFull.Award>) {
		this.map { it.asJsonObject }.mapTo(awards) {
			AutorFull.Award(
					it.get("award_id").getField()?.asInt ?: -1,
					it.get("award_in_list").getField()?.asInt == 1,
					it.get("award_is_opened").getField()?.asInt == 1,
					it.get("award_name").getField()?.asString ?: "",
					it.get("award_rusname").getField()?.asString ?: "",
					it.get("contest_id").getField()?.asInt ?: -1,
					it.get("contest_name").getField()?.asString ?: "",
					it.get("contest_year").getField()?.asInt ?: -1,
					it.get("cw_id").getField()?.asInt ?: -1,
					it.get("cw_is_winner").getField()?.asInt == 1,
					it.get("cw_postfix").getField()?.asString ?: "",
					it.get("cw_prefix").getField()?.asString ?: "",
					it.get("nomination_id").getField()?.asInt ?: -1,
					it.get("nomination_name").getField()?.asString ?: "",
					it.get("nomination_rusname").getField()?.asString ?: "",
					it.get("work_id").getField()?.asInt ?: -1,
					it.get("work_name").getField()?.asString ?: "",
					it.get("work_rusname").getField()?.asString ?: "",
					it.get("work_year").getField()?.asInt ?: -1
			)
		}
	}

	fun JsonObject.parseStat(): AutorFull.Stat {
		return AutorFull.Stat(
				this.get("awardcount").getField()?.asInt ?: -1,
				this.get("editioncount").getField()?.asInt ?: -1,
				this.get("moviecount").getField()?.asInt ?: -1,
				this.get("markcount").getField()?.asInt ?: -1,
				this.get("responsecount").getField()?.asInt ?: -1
		)
	}

	fun JsonObject.parseAllWorks(works: SparseArray<List<AutorFull.Work>>) {
		for ((key, value) in this.entrySet()) {
			val worksList = ArrayList<AutorFull.Work>()
			value.asJsonObject.getAsJsonArray("list").parseWorks(worksList)
			works.put(key.toInt(), worksList)
		}
	}

	fun JsonArray.parseWorks(works: ArrayList<AutorFull.Work>) {
		for (work in this) {
			val workObject = work.asJsonObject
			val autorLinks = ArrayList<AutorFull.AutorLink>()
			workObject.getAsJsonArray("authors")?.map { it.asJsonObject }?.mapTo(autorLinks) {
				AutorFull.AutorLink(
						it.get("id").asInt,
						it.get("name").asString
				)
			}
			works.add(AutorFull.Work(
					autorLinks,
					workObject.get("val_midmark").getField()?.asFloat ?: -1F,
					workObject.get("val_responsecount").getField()?.asInt ?: -1,
					workObject.get("val_voters").getField()?.asInt ?: -1,
					workObject.get("work_description").getField()?.asString ?: "",
					workObject.get("work_id").getField()?.asInt ?: -1,
					workObject.get("work_name").getField()?.asString ?: "",
					workObject.get("work_name_alt").getField()?.asString ?: "",
					workObject.get("work_name_orig").getField()?.asString ?: "",
					workObject.get("work_name_bonus").getField()?.asString ?: "",
					workObject.get("work_notfinished").getField()?.asInt == 1,
					workObject.get("work_published").getField()?.asInt == 1,
					workObject.get("work_preparing").getField()?.asInt == 1,
					workObject.get("work_type_id").getField()?.asInt ?: -1,
					workObject.get("work_year").getField()?.asInt ?: -1,
					workObject.get("work_year_of_write").getField()?.asInt ?: -1,
					workObject.get("deep").getField()?.asInt ?: 0,
					workObject.get("plus").getField()?.asInt == 1,
					workObject.get("public_download_file").getField()?.asInt == 1,
					workObject.get("work_lp").getField()?.asInt == 1,
					workObject.get("publish_for_children").getField()?.asInt == 1
			))

			workObject.getAsJsonArray("children")?.parseWorks(works)
		}
	}

	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): AutorFull {
		val jsonObject = json.asJsonObject

		val awards = ArrayList<AutorFull.Award>()
		jsonObject.getAsJsonObject("awards")?.getAsJsonArray("nom")?.parseAwards(awards)
		jsonObject.getAsJsonObject("awards")?.getAsJsonArray("win")?.parseAwards(awards)

		val biography = AutorFull.Biography(
				jsonObject.get("anons").getField()?.asString ?: "",
				jsonObject.get("biography").getField()?.asString ?: "",
				jsonObject.get("biography_notes").getField()?.asString ?: "",
				jsonObject.get("source").getField()?.asString ?: "",
				jsonObject.get("source_link").getField()?.asString ?: "",
				jsonObject.get("birthday").getField()?.asString?.parseToDate() ?: "0000-00-00".parseToDate(),
				jsonObject.get("name").getField()?.asString ?: "",
				jsonObject.get("name_orig").getField()?.asString ?: "",
				jsonObject.get("name_rp").getField()?.asString ?: "",
				jsonObject.get("name_short").getField()?.asString ?: "",
				jsonObject.get("sex").getField()?.asString ?: ""
		)

		val country = AutorFull.Country(
				jsonObject.get("country_id").getField()?.asInt ?: -1,
				jsonObject.get("country_name").getField()?.asString ?: ""
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
					it.get("descr").asString,
					it.get("site").asString
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