package org.odddev.fantlab.core.utils

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.odddev.fantlab.autors.autor.AutorFull
import java.util.*

fun JsonElement?.getField(): JsonElement? =
		if (this == null || this.isJsonNull || this.asString.isEmpty()) null else this

fun String.parseToDate(): Calendar = Calendar.getInstance().apply {
	set(Calendar.YEAR, this@parseToDate.substring(0..3).toInt())
	set(Calendar.MONTH, this@parseToDate.substring(5..6).toInt() - 1)
	set(Calendar.DAY_OF_MONTH, this@parseToDate.substring(8..9).toInt())
	set(Calendar.HOUR, 0)
	set(Calendar.MINUTE, 0)
	set(Calendar.SECOND, 0)
}

fun JsonArray.parseAwards(awards: ArrayList<AutorFull.Award>) {
	this.map { it.asJsonObject }.mapTo(awards) {
		AutorFull.Award(
				id = it.get("award_id").getField()?.asInt ?: -1,
				inList = it.get("award_in_list").getField()?.asInt == 1,
				isOpened = it.get("award_is_opened").getField()?.asInt == 1,
				name = it.get("award_name").getField()?.asString ?: "",
				rusName = it.get("award_rusname").getField()?.asString ?: "",
				contestId = it.get("contest_id").getField()?.asInt ?: -1,
				contestName = it.get("contest_name").getField()?.asString ?: "",
				contestYear = it.get("contest_year").getField()?.asInt ?: -1,
				cwId = it.get("cw_id").getField()?.asInt ?: -1,
				cwIsWinner = it.get("cw_is_winner").getField()?.asInt == 1,
				cwPostfix = it.get("cw_postfix").getField()?.asString ?: "",
				cwPrefix = it.get("cw_prefix").getField()?.asString ?: "",
				nominationId = it.get("nomination_id").getField()?.asInt ?: -1,
				nominationName = it.get("nomination_name").getField()?.asString ?: "",
				nominationRusname = it.get("nomination_rusname").getField()?.asString ?: "",
				workId = it.get("work_id").getField()?.asInt ?: -1,
				workName = it.get("work_name").getField()?.asString ?: "",
				workRusname = it.get("work_rusname").getField()?.asString ?: "",
				workYear = it.get("work_year").getField()?.asInt ?: -1
		)
	}
}

fun JsonObject.parseStat(): AutorFull.Stat = AutorFull.Stat(
		award = this.get("awardcount").getField()?.asInt ?: -1,
		edition = this.get("editioncount").getField()?.asInt ?: -1,
		movie = this.get("moviecount").getField()?.asInt ?: -1,
		mark = this.get("markcount").getField()?.asInt ?: -1,
		response = this.get("responsecount").getField()?.asInt ?: -1
)

fun JsonArray.parseWorks(works: ArrayList<AutorFull.Work>) {
	for (work in this) {
		val workObject = work.asJsonObject
		val autorLinks = ArrayList<AutorFull.AutorLink>()
		workObject.getAsJsonArray("authors")?.map { it.asJsonObject }?.mapTo(autorLinks) {
			AutorFull.AutorLink(
					id = it.get("id").asInt,
					name = it.get("name").asString
			)
		}
		works.add(AutorFull.Work(
				autors = autorLinks,
				midmark = workObject.get("val_midmark").getField()?.asFloat ?: -1F,
				rating = workObject.get("val_midmark_rating").getField()?.asFloat ?: -1F,
				responseCount = workObject.get("val_responsecount").getField()?.asInt ?: -1,
				voters = workObject.get("val_voters").getField()?.asInt ?: -1,
				description = workObject.get("work_description").getField()?.asString ?: "",
				id = workObject.get("work_id").getField()?.asInt ?: -1,
				name = workObject.get("work_name").getField()?.asString ?: "",
				nameAlt = workObject.get("work_name_alt").getField()?.asString ?: "",
				nameOrig = workObject.get("work_name_orig").getField()?.asString ?: "",
				nameBonus = workObject.get("work_name_bonus").getField()?.asString ?: "",
				notFinished = workObject.get("work_notfinished").getField()?.asInt == 1,
				published = workObject.get("work_published").getField()?.asInt == 1,
				preparing = workObject.get("work_preparing").getField()?.asInt == 1,
				type = workObject.get("work_type_id").getField()?.asInt ?: -1,
				year = workObject.get("work_year").getField()?.asInt ?: -1,
				writeYear = workObject.get("work_year_of_write").getField()?.asInt ?: -1,
				deep = workObject.get("deep").getField()?.asInt ?: 0,
				plus = workObject.get("plus").getField()?.asInt == 1,
				canDownload = workObject.get("public_download_file").getField()?.asInt == 1,
				hasLp = workObject.get("work_lp").getField()?.asInt == 1,
				forChildren = workObject.get("publish_for_children").getField()?.asInt == 1
		))

		workObject.getAsJsonArray("children")?.parseWorks(works)
	}
}
