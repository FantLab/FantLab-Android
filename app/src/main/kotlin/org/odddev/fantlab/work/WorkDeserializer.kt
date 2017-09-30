package org.odddev.fantlab.work

import com.google.gson.*
import org.odddev.fantlab.autors.Autor
import org.odddev.fantlab.autors.autor.AutorFull
import org.odddev.fantlab.core.utils.getField
import org.odddev.fantlab.core.utils.parseAwards
import org.odddev.fantlab.core.utils.parseStat
import org.odddev.fantlab.core.utils.parseBlockWorks
import org.odddev.fantlab.edition.Edition
import java.lang.reflect.Type

class WorkDeserializer : JsonDeserializer<Work> {

	fun JsonArray.parseAutors(autors: ArrayList<Autor>) {
		this.map { it.asJsonObject }.mapTo(autors) {
			Autor(
					id = it.get("id").asInt,
					name = it.get("name").asString,
					nameOrig = it.get("name_orig").asString,
					type = it.get("type").asString,
					isOpened = it.get("is_opened").asInt == 1
			)
		}
	}

	fun JsonArray.parseEditions(editions: ArrayList<Edition>) {
		val isbns = ArrayList<String>()

		this.map { it.asJsonObject }.mapTo(editions) {
			Edition(
					autors = it.get("autors").asString,
					compiers = it.get("compilers").asString,
					translators = it.get("translators").asString,
					correctLevel = it.get("correct").asFloat,
					covertType = it.get("cover_type").asString, // shit. type is int actual here
					ebook = it.get("ebook").asInt == 1,
					id = it.get("edition_id").asInt,
					isbns = it.get("isbn").asString.mapTo(isbns) { it.toString() }, // some shit
					language = Edition.Language(
							id = it.get("lang_id").asInt,
							code = it.get("lang_code").asString,
							name = it.get("lang").asString
					),
					origName = it.get("name").asString,
					type = it.get("type").asInt,
					year = it.get("year").asInt
			)
		}
	}

	fun JsonObject.parseRating(): Work.Rating = Work.Rating(
			rating = this.get("rating").getField()?.asFloat ?: -1f,
			voters = this.get("voters").getField()?.asInt ?: -1
	)

	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Work {
		val jsonObject = json.asJsonObject

		val autors = ArrayList<Autor>()
		jsonObject.getAsJsonArray("authors").parseAutors(autors)

		val awards = java.util.ArrayList<AutorFull.Award>()
		jsonObject.getAsJsonObject("awards")?.getAsJsonArray("nom")?.parseAwards(awards)
		jsonObject.getAsJsonObject("awards")?.getAsJsonArray("win")?.parseAwards(awards)

		val children = ArrayList<AutorFull.Work>()
		jsonObject.getAsJsonArray("children").parseBlockWorks(children)

		val editions = ArrayList<Edition>()
		jsonObject.getAsJsonArray("editions").parseEditions(editions)

		val stat = jsonObject.getAsJsonObject("stat").parseStat()

		val saga = ArrayList<String>()
		jsonObject.getAsJsonArray("saga").mapTo(saga) { it.asString }

		val rating = jsonObject.getAsJsonObject("rating").parseRating()

		val description = Work.Description(
				text = jsonObject.get("work_description").asString,
				autor = jsonObject.get("work_description_author").asString
		)

		return Work(
				autors = autors,
				awards = awards,
				children = children,
				editions = editions,
				stat = stat,
				saga = saga,
				rating = rating,
				responseCount = jsonObject.get("val_responsecount").asInt,
				id = jsonObject.get("work_id").asInt,
				name = jsonObject.get("work_name").asString,
				nameBonus = jsonObject.get("work_name_bonus").asString,
				nameOrig = jsonObject.get("work_name_orig").asString,
				description = description,
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