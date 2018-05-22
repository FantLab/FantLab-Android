package ru.fantlab.android.provider.rest

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rx_object
import com.google.gson.Gson
import ru.fantlab.android.data.dao.response.*

object DataManager {

	val gson = Gson()

	fun getAuthors() = "/autorsall"
			.httpGet()
			.rx_object(AuthorsResponse.Deserializer())

	fun getAuthor(
			id: Int,
			showBiography: Boolean = false,
			showAwards: Boolean = false,
			showLinguaProfile: Boolean = false,
			showBiblioBlocks: Boolean = false,
			sortOption: BiblioSortOption = BiblioSortOption.BY_YEAR
	) = "/autor/$id"
			.httpGet(listOf(
					"biography" to showBiography.toInt(),
					"awards" to showAwards.toInt(),
					"la_resume" to showLinguaProfile.toInt(),
					"biblio_blocks" to showBiblioBlocks.toInt(),
					"sort" to sortOption.value
			))
			.rx_object(AuthorResponse.Deserializer())

	fun getAuthorEditions(
			authorId: Int,
			showEditionsBlocks: Boolean = false
	) = "/autor/$authorId/alleditions"
			.httpGet(listOf(
					"editions_blocks" to showEditionsBlocks.toInt()
			))
			.rx_object(AuthorEditionsResponse.Deserializer())

	fun getAuthorResponses(
			authorId: Int,
			page: Int = 1,
			sortOption: ResponsesSortOption = ResponsesSortOption.BY_DATE
	) = "/autor/$authorId/responses"
			.httpGet(listOf(
					"page" to page,
					"sort" to sortOption.value
			))
			.rx_object(ResponsesResponse.Deserializer(perPage = 50))

	fun getWork(
			id: Int,
			showAwards: Boolean = false,
			showChildren: Boolean = false,
			showClassificatory: Boolean = false,
			showEditionsBlocks: Boolean = false,
			showEditionsInfo: Boolean = false,
			showFilms: Boolean = false,
			showLinguaProfile: Boolean = false,
			showParents: Boolean = false,
			showTranslations: Boolean = false
	) = "/work/$id"
			.httpGet(listOf(
					"awards" to showAwards.toInt(),
					"children" to showChildren.toInt(),
					"classificatory" to showClassificatory.toInt(),
					"editions_blocks" to showEditionsBlocks.toInt(),
					"editions_info" to showEditionsInfo.toInt(),
					"films" to showFilms.toInt(),
					"la_resume" to showLinguaProfile.toInt(),
					"parents" to showParents.toInt(),
					"translations" to showTranslations.toInt()
			))
			.rx_object(WorkResponse.Deserializer())

	fun getWorkResponses(
			workId: Int,
			page: Int = 1,
			sortOption: ResponsesSortOption = ResponsesSortOption.BY_RATING
	) = "/work/$workId/responses"
			.httpGet(listOf(
					"page" to page,
					"sort" to sortOption.value
			))
			.rx_object(ResponsesResponse.Deserializer(perPage = 15))

	fun getWorkAnalogs(
			workId: Int
	) = "/work/$workId/analogs"
			.httpGet()
			.rx_object(WorkAnalogsResponse.Deserializer())

	fun getEdition(
			id: Int,
			showContent: Boolean = false,
			showAdditionalImages: Boolean = false
	) = "/edition/$id"
			.httpGet(listOf(
					"content" to showContent.toInt(),
					"images_plus" to showAdditionalImages.toInt()
			))
			.rx_object(EditionResponse.Deserializer())

	// getUser

	// getUserMarks

	// getUserResponses

	// login

	// getUserId

	fun searchAuthors(
			query: String,
			page: Int = 1
	) = "/search-autors"
			.httpGet(listOf(
					"q" to query,
					"page" to page
			))
			.rx_object(SearchAuthorsResponse.Deserializer(perPage = 25))

	fun searchWorks(
			query: String,
			page: Int = 1
	) = "search-works"
			.httpGet(listOf(
					"q" to query,
					"page" to page
			))
			.rx_object(SearchWorksResponse.Deserializer(perPage = 25))

	fun searchEditions(
			query: String,
			page: Int = 1
	) = "search-editions"
			.httpGet(listOf(
					"q" to query,
					"page" to page
			))
			.rx_object(SearchEditionsResponse.Deserializer(perPage = 25))

	// getLastResponses
}

enum class BiblioSortOption(val value: String) {
	BY_YEAR("year"),
	BY_RATING("rating"),
	BY_MARK_COUNT("markcount"),
	BY_RUS_NAME("rusname"),
	BY_NAME("name"),
	BY_WRITE_YEAR("writeyear")
}

enum class ResponsesSortOption(val value: String) {
	BY_DATE("date"),
	BY_RATING("rating"),
	BY_MARK("mark")
}

fun Boolean.toInt(): Int = if (this) 1 else 0