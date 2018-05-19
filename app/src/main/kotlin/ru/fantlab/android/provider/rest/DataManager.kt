package ru.fantlab.android.provider.rest

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rx_object
import com.google.gson.Gson
import ru.fantlab.android.data.dao.response.AuthorResponse
import ru.fantlab.android.data.dao.response.EditionResponse
import ru.fantlab.android.data.dao.response.WorkResponse

object DataManager {

	val gson = Gson()

	// getAuthors

	fun getAuthor(
			id: Int,
			showBiography: Boolean = false,
			showAwards: Boolean = false,
			showLinguaProfile: Boolean = false,
			showBiblioBlocks: Boolean = false,
			sortOption: String = "year"
	) = "/autor/$id".toAbsolutePath()
			.httpGet(listOf(
					"biography" to showBiography.toInt(),
					"awards" to showAwards.toInt(),
					"la_resume" to showLinguaProfile.toInt(),
					"biblio_blocks" to showBiblioBlocks.toInt(),
					"sort" to sortOption
			))
			.rx_object(AuthorResponse.Deserializer())

	// getAuthorEditions

	// getAuthorResponses

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
	) = "/work/$id".toAbsolutePath()
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

	// getWorkResponses

	// getWorkAnalogs

	fun getEdition(
			id: Int,
			showContent: Boolean = false,
			showAdditionalImages: Boolean = false
	) = "/edition/$id".toAbsolutePath()
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

	// searchAuthors

	// searchWorks

	// searchEditions

	// getLastResponses
}

fun String.toAbsolutePath() = "https://api.fantlab.ru$this"

fun Boolean.toInt(): Int = if (this) 1 else 0