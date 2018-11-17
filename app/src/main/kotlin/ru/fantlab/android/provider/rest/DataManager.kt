package ru.fantlab.android.provider.rest

import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.rx.rx_object
import com.github.kittinunf.fuel.rx.rx_response
import com.github.kittinunf.fuel.rx.rx_string
import com.google.gson.Gson
import io.reactivex.Single
import ru.fantlab.android.data.dao.response.*

object DataManager {

	val gson = Gson()

	fun getAuthors(): Single<AuthorsResponse> =
			getAuthorsPath()
					.httpGet()
					.rx_object(AuthorsResponse.Deserializer())
					.map { it.get() }

	fun getAwards(
			nonfant: Boolean,
			sortOption: AwardsSortOption = AwardsSortOption.BY_NAME
	): Single<AwardsResponse> =
			getAwardsPath(nonfant, sortOption)
					.httpGet()
					.rx_object(AwardsResponse.Deserializer())
					.map { it.get() }

	fun getAuthor(
			id: Int,
			showBiography: Boolean = false,
			showAwards: Boolean = false,
			showLinguaProfile: Boolean = false,
			showBiblioBlocks: Boolean = false,
			sortOption: BiblioSortOption = BiblioSortOption.BY_YEAR
	): Single<AuthorResponse> =
			getAuthorPath(id, showBiography, showAwards, showLinguaProfile, showBiblioBlocks, sortOption)
					.httpGet()
					.rx_object(AuthorResponse.Deserializer())
					.map { it.get() }

	fun getAuthorEditions(
			authorId: Int,
			showEditionsBlocks: Boolean = false
	): Single<AuthorEditionsResponse> =
			getAuthorEditionsPath(authorId, showEditionsBlocks)
					.httpGet()
					.rx_object(AuthorEditionsResponse.Deserializer())
					.map { it.get() }

	fun getAuthorResponses(
			authorId: Int,
			page: Int = 1,
			sortOption: ResponsesSortOption = ResponsesSortOption.BY_DATE
	): Single<ResponsesResponse> =
			getAuthorResponsesPath(authorId, page, sortOption)
					.httpGet()
					.rx_object(ResponsesResponse.Deserializer(perPage = 50))
					.map { it.get() }

	fun getAward(
			id: Int,
			showNomi: Boolean,
			showContests: Boolean,
			sortOption: AwardSortOption = AwardSortOption.BY_CONTEST
	): Single<AwardResponse> =
			getAwardPath(id, showNomi, showContests, sortOption)
					.httpGet()
					.rx_object(AwardResponse.Deserializer())
					.map { it.get() }

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
	): Single<WorkResponse> =
			getWorkPath(id, showAwards, showChildren, showClassificatory, showEditionsBlocks,
					showEditionsInfo, showFilms, showLinguaProfile, showParents, showTranslations)
					.httpGet()
					.rx_object(WorkResponse.Deserializer())
					.map { it.get() }

	fun getWorkResponses(
			workId: Int,
			page: Int = 1,
			sortOption: ResponsesSortOption = ResponsesSortOption.BY_RATING
	): Single<ResponsesResponse> =
			getWorkResponsesPath(workId, page, sortOption)
					.httpGet()
					.rx_object(ResponsesResponse.Deserializer(perPage = 15))
					.map { it.get() }

	fun getWorkAnalogs(
			workId: Int
	): Single<WorkAnalogsResponse> =
			getWorkAnalogsPath(workId)
					.httpGet()
					.rx_object(WorkAnalogsResponse.Deserializer())
					.map { it.get() }

	fun getEdition(
			id: Int,
			showContent: Boolean = false,
			showAdditionalImages: Boolean = false
	): Single<EditionResponse> =
			getEditionPath(id, showContent, showAdditionalImages)
					.httpGet()
					.rx_object(EditionResponse.Deserializer())
					.map { it.get() }

	fun getUser(
			id: Int
	): Single<UserResponse> =
			getUserPath(id)
					.httpGet()
					.rx_object(UserResponse.Deserializer())
					.map { it.get() }

	fun getUserMarks(
			userId: Int,
			page: Int = 1,
			typeOption: MarksTypeOption = MarksTypeOption.ALL,
			sortOption: MarksSortOption = MarksSortOption.BY_MARK
	): Single<MarksResponse> =
			getUserMarksPath(userId, page, typeOption, sortOption)
					.httpGet()
					.rx_object(MarksResponse.Deserializer(perPage = 200))
					.map { it.get() }

	fun getUserMarksMini(
			userId: Int,
			workIds: String
	): Single<MarksMiniResponse> =
			getUserMarksMiniPath(userId, workIds)
					.httpGet()
					.rx_object(MarksMiniResponse.Deserializer())
					.map { it.get() }

	fun getUserResponses(
			userId: Int,
			page: Int = 1,
			sortOption: ResponsesSortOption = ResponsesSortOption.BY_DATE
	): Single<ResponsesResponse> =
			getUserResponsesPath(userId, page, sortOption)
					.httpGet()
					.rx_object(ResponsesResponse.Deserializer(perPage = 50))
					.map { it.get() }

	fun sendUserMark(
			workId: Int,
			toWorkId: Int,
			mark: Int
	): Single<MarkResponse> =
			sendUserMarkPath(workId, toWorkId, mark)
					.httpGet()
					.rx_object(MarkResponse.Deserializer())
					.map { it.get() }

	fun sendResponseVote(
			responseId: Int,
			voteType: String
	): Single<String> =
			sendResponseVotePath(responseId, voteType)
					.httpGet()
					.rx_string()
					.map { it.get() }

	fun sendMessage(
			userId: Int,
			message: CharSequence?,
			mode: String
	): Single<String> =
			sendMessagePath(userId, message, mode)
					.httpPost()
					.rx_string()
					.map { it.get() }

	fun sendResponse(
			workId: Int,
			message: CharSequence?,
			mode: String
	): Single<String> =
			sendResponsePath(workId, message, mode)
					.httpPost()
					.rx_string()
					.map { it.get() }

	fun sendClassification(
			workId: Int,
			query: String
	): Single<String> =
			sendClassificationPath(workId, query)
					.httpGet()
					.rx_string()
					.map { it.get() }

	fun login(
			login: String,
			password: String
	): Single<Response> =
			loginPath(login, password)
					.httpPost()
					.rx_response()
					.map { it.first }

	fun getUserId(
			login: String
	): Single<UserIdResponse> =
			getUserIdPath(login)
					.httpGet()
					.rx_object(UserIdResponse.Deserializer())
					.map { it.get() }

	fun searchAuthors(
			query: String,
			page: Int = 1
	): Single<SearchAuthorsResponse> =
			searchAuthorsPath(query, page)
					.httpGet()
					.rx_object(SearchAuthorsResponse.Deserializer(perPage = 25))
					.map { it.get() }

	fun searchWorks(
			query: String,
			page: Int = 1
	): Single<SearchWorksResponse> =
			searchWorksPath(query, page)
					.httpGet()
					.rx_object(SearchWorksResponse.Deserializer(perPage = 25))
					.map { it.get() }

	fun searchEditions(
			query: String,
			page: Int = 1
	): Single<SearchEditionsResponse> =
			searchEditionsPath(query, page)
					.httpGet()
					.rx_object(SearchEditionsResponse.Deserializer(perPage = 25))
					.map { it.get() }

	fun searchAwards(
			query: String,
			page: Int = 1
	): Single<SearchAwardsResponse> =
			searchAwardsPath(query, page)
					.httpGet()
					.rx_object(SearchAwardsResponse.Deserializer(perPage = 25))
					.map { it.get() }

	fun getLastResponses(
			page: Int = 1
	): Single<ResponsesResponse> =
			getLastResponsesPath(page)
					.httpGet()
					.rx_object(ResponsesResponse.Deserializer(perPage = 50))
					.map { it.get() }
}

//region Sort options
enum class BiblioSortOption(val value: String) {
	BY_YEAR("year"),
	BY_RATING("rating"),
	BY_MARK_COUNT("markcount"),
	BY_RUS_NAME("rusname"),
	BY_NAME("name"),
	BY_WRITE_YEAR("writeyear")
}

enum class MarksTypeOption(val value: String) {
	ALL("all"),
	NOVEL("novel"),
	STORY("story")
}

enum class MarksSortOption(val value: String) {
	BY_MARK("mark"),
	BY_NAME("name"),
	BY_AUTHOR("autor"),
	BY_DATE("date"),
	BY_YEAR("year")
}

enum class ResponsesSortOption(val value: String) {
	BY_DATE("date"),
	BY_RATING("rating"),
	BY_MARK("mark")
}

enum class AwardsSortOption(val value: String) {
	BY_NAME("name"),
	BY_COUNTRY("country"),
	BY_TYPE("type"),
	BY_LANG("lang")
}

enum class AwardSortOption(val value: String) {
	BY_CONTEST("contest"),
	BY_NOMI("nomi")
}
//endregion

//region Urls
fun getAuthorsPath() =
		"/autorsall".toAbsolutePathWithApiVersion()

fun getAwardsPath(
		nonfant: Boolean,
		sortOption: AwardsSortOption = AwardsSortOption.BY_NAME
) = "/awards?nonfant=${nonfant.toInt()}&sort=${sortOption.value}".toAbsolutePathWithApiVersion()

fun getAuthorPath(
		id: Int,
		showBiography: Boolean = false,
		showAwards: Boolean = false,
		showLinguaProfile: Boolean = false,
		showBiblioBlocks: Boolean = false,
		sortOption: BiblioSortOption = BiblioSortOption.BY_YEAR
) = ("/autor/$id?biography=${showBiography.toInt()}&awards=${showAwards.toInt()}" +
		"&la_resume=${showLinguaProfile.toInt()}&biblio_blocks=${showBiblioBlocks.toInt()}" +
		"&sort=${sortOption.value}").toAbsolutePathWithApiVersion()

fun getAuthorEditionsPath(
		authorId: Int,
		showEditionsBlocks: Boolean = false
) = "/autor/$authorId/alleditions?editions_blocks=${showEditionsBlocks.toInt()}"
		.toAbsolutePathWithApiVersion()

fun getAuthorResponsesPath(
		authorId: Int,
		page: Int = 1,
		sortOption: ResponsesSortOption = ResponsesSortOption.BY_DATE
) = "/autor/$authorId/responses?page=$page&sort=${sortOption.value}".toAbsolutePathWithApiVersion()

fun getAwardPath(
		id: Int,
		showNomi: Boolean,
		showContests: Boolean,
		sortOption: AwardSortOption = AwardSortOption.BY_CONTEST
) = ("/award/$id?include_nomi=${showNomi.toInt()}&include_contests=${showContests.toInt()}" +
		"&sort=${sortOption.value}").toAbsolutePathWithApiVersion()

fun getWorkPath(
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
) = ("/work/$id?awards=${showAwards.toInt()}&children=${showChildren.toInt()}" +
		"&classificatory=${showClassificatory.toInt()}&editions_blocks=${showEditionsBlocks.toInt()}" +
		"&editions_info=${showEditionsInfo.toInt()}&films=${showFilms.toInt()}" +
		"&la_resume=${showLinguaProfile.toInt()}&parents=${showParents.toInt()}" +
		"&translations=${showTranslations.toInt()}").toAbsolutePathWithApiVersion()

fun getWorkResponsesPath(
		workId: Int,
		page: Int = 1,
		sortOption: ResponsesSortOption = ResponsesSortOption.BY_RATING
) = "/work/$workId/responses?page=$page&sort=${sortOption.value}".toAbsolutePathWithApiVersion()

fun getWorkAnalogsPath(
		workId: Int
) = "/work/$workId/analogs".toAbsolutePathWithApiVersion()

fun getEditionPath(
		id: Int,
		showContent: Boolean = false,
		showAdditionalImages: Boolean = false
) = "/edition/$id?content=${showContent.toInt()}&images_plus=${showAdditionalImages.toInt()}"
		.toAbsolutePathWithApiVersion()

fun getUserPath(
		id: Int
) = "/user/$id".toAbsolutePathWithApiVersion()

fun getUserMarksPath(
		userId: Int,
		page: Int = 1,
		typeOption: MarksTypeOption = MarksTypeOption.ALL,
		sortOption: MarksSortOption = MarksSortOption.BY_MARK
) = "/user/$userId/marks/extended?page=$page&type=${typeOption.value}&sort=${sortOption.value}"
		.toAbsolutePathWithApiVersion()

fun getUserMarksMiniPath(
		userId: Int,
		workIds: String
) = "/user/$userId/marks?w=$workIds&mini=1".toAbsolutePathWithApiVersion()

fun getUserResponsesPath(
		userId: Int,
		page: Int = 1,
		sortOption: ResponsesSortOption = ResponsesSortOption.BY_DATE
) = "/user/$userId/responses?page=$page&sort=${sortOption.value}".toAbsolutePathWithApiVersion()

fun sendUserMarkPath(
		workId: Int,
		toWorkId: Int,
		mark: Int
) = "/work$workId/ajaxsetmark${mark}towork$toWorkId".toAbsolutePath()

fun sendResponseVotePath(
		responseId: Int,
		voteType: String
) = "/vote$responseId$voteType".toAbsolutePath()

fun sendMessagePath(
		userId: Int,
		message: CharSequence?,
		mode: String
) = "/user$userId/sendprivatemessage?message=$message&mode=$mode&action=/user$userId/sendprivatemessage"
		.toAbsolutePath()

fun sendResponsePath(
		workId: Int,
		message: CharSequence?,
		mode: String
) = "/work$workId/addresponse?message=$message&mode=$mode&autosave=0".toAbsolutePath()

fun sendClassificationPath(
		workId: Int,
		query: String
) = "/genrevote$workId?$query".toAbsolutePath()

fun loginPath(
		login: String,
		password: String
) = "/login?login=$login&password=$password".toAbsolutePath()

fun getUserIdPath(
		login: String
) = "/userlogin?usersearch=$login".toAbsolutePathWithApiVersion()

fun searchAuthorsPath(
		query: String,
		page: Int = 1
) = "/search-autors?q=$query&page=$page".toAbsolutePathWithApiVersion()

fun searchWorksPath(
		query: String,
		page: Int = 1
) = "/search-works?q=$query&page=$page".toAbsolutePathWithApiVersion()

fun searchEditionsPath(
		query: String,
		page: Int = 1
) = "/search-editions?q=$query&page=$page".toAbsolutePathWithApiVersion()

fun searchAwardsPath(
		query: String,
		page: Int = 1
) = "/search-awards?q=$query&page=$page".toAbsolutePathWithApiVersion()

fun getLastResponsesPath(
		page: Int = 1
) = "/responses?page=$page".toAbsolutePathWithApiVersion()
//endregion

//region Utils
fun String.toAbsolutePath() = "https://fantlab.ru$this"

fun String.toAbsolutePathWithApiVersion() = "https://api.fantlab.ru$this"

fun Boolean.toInt(): Int = if (this) 1 else 0
//endregion