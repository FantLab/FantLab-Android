package org.odddev.fantlab.search

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class SearchResultDeserializer : JsonDeserializer<SearchResult> {

	override fun deserialize(
			json: JsonElement,
			typeOfT: Type?,
			context: JsonDeserializationContext?
	): SearchResult {
		val coreArray = json.asJsonArray
		val authorMatchesObject = coreArray[0].asJsonObject
		val authorMatchesCount = authorMatchesObject["total"].asInt
		val authorMatches = authorMatchesObject["matches"].asJsonArray
		val authorsSearchResult = ArrayList<AuthorSearchResult>()
		authorMatches
				.map { it.asJsonObject }
				.mapTo(authorsSearchResult) {
					AuthorSearchResult(
							it["autor_id"].asInt,
							it["birthyear"].asInt,
							it["country"].asString,
							it["country_id"].asInt,
							it["deathyear"].asInt,
							it["editioncount"].asInt,
							it["is_opened"].asInt == 1,
							it["markcount"].asInt,
							it["midmark"].asInt,
							it["moviecount"].asInt,
							it["name"].asString,
							it["pseudo_names"].asString,
							it["responsecount"].asInt,
							it["rusname"].asString
					)
				}

		val workMatchesObject = coreArray[1].asJsonObject
		val workMatchesCount = workMatchesObject["total"].asInt
		val workMatches = workMatchesObject["matches"].asJsonArray
		val worksSearchResult = ArrayList<WorkSearchResult>()
		workMatches
				.map { it.asJsonObject }
				.mapTo(worksSearchResult) {
					WorkSearchResult(
							it["all_autor_name"].asString,
							it["all_autor_rusname"].asString,
							it["altname"].asString,
							it["autor1_id"].asInt,
							it["autor1_is_opened"].asInt == 1,
							it["autor1_rusname"].asString,
							it["autor2_id"].asInt,
							it["autor2_is_opened"].asInt == 1,
							it["autor2_rusname"].asString,
							it["autor3_id"].asInt,
							it["autor3_is_opened"].asInt == 1,
							it["autor3_rusname"].asString,
							it["autor4_id"].asInt,
							it["autor4_is_opened"].asInt == 1,
							it["autor4_rusname"].asString,
							it["autor5_id"].asInt,
							it["autor5_is_opened"].asInt == 1,
							it["autor5_rusname"].asString,
							it["markcount"].asInt,
							it["midmark"].asJsonArray[0].asFloat,
							it["name"].asString,
							it["name_show_im"].asString,
							it["rusname"].asString,
							it["work_id"].asInt,
							it["year"].asInt
					)
				}

		val editionMatchesObject = coreArray[2].asJsonObject
		val editionMatchesCount = editionMatchesObject["total"].asInt
		val editionMatches = editionMatchesObject["matches"].asJsonArray
		val editionsSearchResult = ArrayList<EditionSearchResult>()
		editionMatches
				.map { it.asJsonObject }
				.mapTo(editionsSearchResult) {
					EditionSearchResult(
							it["autors"].asString,
							it["comment"].asString,
							it["compilers"].asString,
							it["correct"].asInt,
							it["edition_id"].asInt,
							it["isbn1"].asString,
							it["name"].asString,
							it["notes"].asString,
							it["plan_date"].asString,
							it["publisher"].asString,
							it["series"].asString,
							it["year"].asInt
					)
				}

		return SearchResult(
				authorsSearchResult = AuthorsSearchResult(authorsSearchResult, authorMatchesCount),
				worksSearchResult = WorksSearchResult(worksSearchResult, workMatchesCount),
				editionsSearchResult = EditionsSearchResult(editionsSearchResult, editionMatchesCount)
		)
	}
}