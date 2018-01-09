package ru.fantlab.android.search

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
							authorId = it["autor_id"].asInt,
							birthYear = it["birthyear"].asInt,
							country = it["country"].asString,
							countryId = it["country_id"].asInt,
							deathYear = it["deathyear"].asInt,
							editionCount = it["editioncount"].asInt,
							isOpened = it["is_opened"].asInt == 1,
							markCount = it["markcount"].asInt,
							midMark = it["midmark"].asInt,
							movieCount = it["moviecount"].asInt,
							name = it["name"].asString,
							pseudoNames = it["pseudo_names"].asString,
							responseCount = it["responsecount"].asInt,
							rusName = it["rusname"].asString
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
							allAuthorName = it["all_autor_name"].asString,
							allAuthorRusName = it["all_autor_rusname"].asString,
							altName = it["altname"].asString,
							author1Id = it["autor1_id"].asInt,
							author1IsOpened = it["autor1_is_opened"].asInt == 1,
							author1RusName = it["autor1_rusname"].asString,
							author2Id = it["autor2_id"].asInt,
							author2IsOpened = it["autor2_is_opened"].asInt == 1,
							author2RusName = it["autor2_rusname"].asString,
							author3Id = it["autor3_id"].asInt,
							author3IsOpened = it["autor3_is_opened"].asInt == 1,
							author3RusName = it["autor3_rusname"].asString,
							author4Id = it["autor4_id"].asInt,
							author4IsOpened = it["autor4_is_opened"].asInt == 1,
							author4RusName = it["autor4_rusname"].asString,
							author5Id = it["autor5_id"].asInt,
							author5IsOpened = it["autor5_is_opened"].asInt == 1,
							author5RusName = it["autor5_rusname"].asString,
							markCount = it["markcount"].asInt,
							midMark = it["midmark"].asJsonArray[0].asFloat,
							name = it["name"].asString,
							workType = it["name_show_im"].asString,
							rusName = it["rusname"].asString,
							workId = it["work_id"].asInt,
							year = it["year"].asInt,
							// todo убрать этот костыль, следует возвращать в api null вместо пустой строки
							coverEditionId = if (it["pic_edition_id_auto"].asString.isNotEmpty())
								it["pic_edition_id_auto"].asInt else 0
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
							authors = it["autors"].asString,
							comment = it["comment"].asString,
							compilers = it["compilers"].asString,
							correct = it["correct"].asInt,
							editionId = it["edition_id"].asInt,
							isbn = it["isbn1"].asString,
							name = it["name"].asString,
							notes = it["notes"].asString,
							planDate = it["plan_date"].asString,
							publisher = it["publisher"].asString,
							series = it["series"].asString,
							year = it["year"].asInt
					)
				}

		return SearchResult(
				authorsSearchResult = AuthorsSearchResult(authorsSearchResult, authorMatchesCount),
				worksSearchResult = WorksSearchResult(worksSearchResult, workMatchesCount),
				editionsSearchResult = EditionsSearchResult(editionsSearchResult, editionMatchesCount)
		)
	}
}