package org.odddev.fantlab.autors.autor

import android.util.SparseArray
import com.google.gson.*
import org.odddev.fantlab.core.models.Author
import org.odddev.fantlab.core.models.Nomination
import org.odddev.fantlab.core.models.Work
import org.odddev.fantlab.core.utils.*
import java.lang.reflect.Type
import java.util.ArrayList

class AuthorInfoDeserializer : JsonDeserializer<AuthorInfo> {

	fun JsonArray.parseNominations(nominations: ArrayList<Nomination>) {
		this.map { it.asJsonObject }.mapTo(nominations) {
			Nomination(
					id = it.get("award_id").asInt,
					contestId = it.get("contest_id").getField()?.asInt,
					contestName = it.get("contest_name").getField()?.asString,
					contestYear = it.get("contest_year").getField()?.asInt,
					cwId = it.get("cw_id").getField()?.asInt,
					cwIsWinner = it.get("cw_is_winner").getField()?.asInt == 1,
					cwPostfix = it.get("cw_postfix").getField()?.asString,
					cwPrefix = it.get("cw_prefix").getField()?.asString,
					inList = it.get("award_in_list").getField()?.asInt == 1,
					isOpened = it.get("award_is_opened").getField()?.asInt == 1,
					name = it.get("award_name").getField()?.asString,
					nominationId = it.get("nomination_id").getField()?.asInt,
					nominationName = it.get("nomination_name").getField()?.asString,
					nominationRusName = it.get("nomination_rusname").getField()?.asString,
					rusName = it.get("award_rusname").getField()?.asString,
					workId = it.get("work_id").getField()?.asInt,
					workName = it.get("work_name").getField()?.asString,
					workRusName = it.get("work_rusname").getField()?.asString,
					workYear = it.get("work_year").getField()?.asInt
			)
		}
	}

	fun JsonObject.parseAllWorks(works: SparseArray<List<Work>>) {
		for ((key, value) in this.entrySet()) {
			val worksList = ArrayList<Work>()
			value.asJsonObject.getAsJsonArray("list").parseWorks(worksList)
			works.put(key.toInt(), worksList)
		}
	}

	fun JsonArray.parseWorks(works: ArrayList<Work>) {
		for (work in this) {
			val workObject = work.asJsonObject
			works.add(
					Work(
					// + authors
					midMark = workObject.get("val_midmark").getField()?.asFloat,
					rating = workObject.get("val_midmark_rating").getField()?.asFloat,
					responseCount = workObject.get("val_responsecount").getField()?.asInt,
					voters = workObject.get("val_voters").getField()?.asInt,
					description = workObject.get("work_description").getField()?.asString,
					id = workObject.get("work_id").asInt,
					name = workObject.get("work_name").getField()?.asString,
					nameAlt = workObject.get("work_name_alt").getField()?.asString,
					nameOrig = workObject.get("work_name_orig").getField()?.asString,
					nameBonus = workObject.get("work_name_bonus").getField()?.asString,
					notFinished = workObject.get("work_notfinished").getField()?.asInt == 1,
					published = workObject.get("work_published").getField()?.asInt == 1,
					preparing = workObject.get("work_preparing").getField()?.asInt == 1,
					type = workObject.get("work_type_id").getField()?.asInt,
					year = workObject.get("work_year").getField()?.asInt,
					writeYear = workObject.get("work_year_of_write").getField()?.asInt,
					deep = workObject.get("deep").getField()?.asInt,
					plus = workObject.get("plus").getField()?.asInt == 1,
					canDownload = workObject.get("public_download_file").getField()?.asInt == 1,
					hasLp = workObject.get("work_lp").getField()?.asInt == 1
			))
			workObject.getAsJsonArray("children")?.parseWorks(works)
		}
	}

	override fun deserialize(
			json: JsonElement,
			typeOfT: Type,
			context: JsonDeserializationContext?
	): AuthorInfo {
		val jsonObject = json.asJsonObject

		val stat = jsonObject.get("stat").asJsonObject
		val author = Author(
				id = jsonObject.get("autor_id").asInt,
				anons = jsonObject.get("anons").getField()?.asString,
				biography = jsonObject.get("biography").getField()?.asString,
				biographyNotes = jsonObject.get("biography_notes").getField()?.asString,
				biographySource = jsonObject.get("source").getField()?.asString,
				biographySourceUrl = jsonObject.get("source_link").getField()?.asString,
				birthDay = jsonObject.get("birthday").getField()?.asString?.parseToDate(),
				compiler = jsonObject.get("compiler").getField()?.asString,
				countryId = jsonObject.get("country_id").getField()?.asInt,
				countryName = jsonObject.get("country_name").getField()?.asString,
				curator = jsonObject.get("curator").getField()?.asInt,
				deathDay = jsonObject.get("deathday").getField()?.asString?.parseToDate(),
				fantastic = jsonObject.get("fantastic").getField()?.asInt,
				isOpened = jsonObject.get("is_opened").getField()?.asInt == 1,
				// + la_resume
				lastModified = jsonObject.get("last_modified").getField()?.asString?.parseToDate(),
				name = jsonObject.get("name").getField()?.asString,
				nameOrig = jsonObject.get("name_orig").getField()?.asString,
				// + name_pseudonyms
				nameRp = jsonObject.get("name_rp").getField()?.asString,
				nameShort = jsonObject.get("name_short").getField()?.asString,
				sex = jsonObject.get("sex").getField()?.asString,
				// + sites
				statAwardCount = stat.get("award_count").getField()?.asInt,
				statEditionCount = stat.get("edition_count").getField()?.asInt,
				statMarkCount = stat.get("mark_count").getField()?.asInt,
				statMovieCount = stat.get("movie_count").getField()?.asInt,
				statResponseCount = stat.get("response_count").getField()?.asInt,
				statWorkCount = stat.get("work_count").getField()?.asInt
		)

		val nominations = arrayListOf<Nomination>()
		val awards = jsonObject.get("awards").asJsonObject
		awards.getAsJsonArray("nom")?.parseNominations(nominations)
		awards.getAsJsonArray("win")?.parseNominations(nominations)

		val works = SparseArray<List<Work>>()
		jsonObject.getAsJsonObject("cycles_blocks")?.parseAllWorks(works)
		jsonObject.getAsJsonObject("works_blocks")?.parseAllWorks(works)

		return AuthorInfo(
				author = author,
				nominations = nominations,
				works = works
		)
	}
}
