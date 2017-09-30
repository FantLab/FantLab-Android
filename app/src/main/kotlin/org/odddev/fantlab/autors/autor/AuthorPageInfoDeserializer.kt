package org.odddev.fantlab.autors.autor

import com.google.gson.*
import org.odddev.fantlab.core.models.*
import org.odddev.fantlab.core.utils.*
import java.lang.reflect.Type
import java.util.ArrayList

class AuthorPageInfoDeserializer : JsonDeserializer<AuthorPageInfo> {

	private fun JsonObject.parseAuthor(
			authors: HashSet<Author>,
			laResume: ArrayList<LaResume>,
			pseudonyms: ArrayList<Pseudonym>,
			sites: ArrayList<Site>
	) {
		val statObject = this.getAsJsonObject("stat")
		authors.add(Author(
				authorId = this.get("autor_id").asInt,
				anons = this.get("anons").getField()?.asString,
				biography = this.get("biography").getField()?.asString,
				biographyNotes = this.get("biography_notes").getField()?.asString,
				biographySource = this.get("source").getField()?.asString,
				biographySourceUrl = this.get("source_link").getField()?.asString,
				birthDay = this.get("birthday").getField()?.asString?.parseToDate(),
				compiler = this.get("compiler").getField()?.asString,
				countryId = this.get("country_id").getField()?.asInt,
				countryName = this.get("country_name").getField()?.asString,
				curator = this.get("curator").getField()?.asInt,
				deathDay = this.get("deathday").getField()?.asString?.parseToDate(),
				fantastic = this.get("fantastic").getField()?.asInt,
				isOpened = this.get("is_opened").getField()?.asInt == 1,
				lastModified = this.get("last_modified").getField()?.asString?.parseToDate(),
				name = this.get("name").getField()?.asString,
				nameOrig = this.get("name_orig").getField()?.asString,
				nameRp = this.get("name_rp").getField()?.asString,
				nameShort = this.get("name_short").getField()?.asString,
				sex = this.get("sex").getField()?.asString,
				statAwardCount = statObject.get("award_count").getField()?.asInt,
				statEditionCount = statObject.get("edition_count").getField()?.asInt,
				statMarkCount = statObject.get("mark_count").getField()?.asInt,
				statMovieCount = statObject.get("movie_count").getField()?.asInt,
				statResponseCount = statObject.get("response_count").getField()?.asInt,
				statWorkCount = statObject.get("work_count").getField()?.asInt
		))
		val laResumeArray = this.getAsJsonArray("la_resume")
		laResumeArray.withIndex().forEach {
			laResume.add(LaResume(
					resume = it.value.asString,
					position = it.index
			))
		}
		val pseudonymsArray = this.getAsJsonArray("name_pseudonyms")
		pseudonymsArray.withIndex().forEach {
			val pseudonymObject = it.value.asJsonObject
			pseudonyms.add(Pseudonym(
					isReal = pseudonymObject.get("is_real").asInt == 1,
					name = pseudonymObject.get("name").asString,
					nameOrig = pseudonymObject.get("name_orig").asString,
					position = it.index
			))
		}
		val sitesArray = this.getAsJsonArray("sites")
		sitesArray.withIndex().forEach {
			val siteObject = it.value.asJsonObject
			sites.add(Site(
					description = siteObject.get("descr").asString,
					url = siteObject.get("site").asString,
					position = it.index
			))
		}
	}
	
	private fun JsonArray.parseNominations(nominations: ArrayList<Nomination>) {
		this.withIndex().forEach {
			val nominationObject = it.value.asJsonObject
			nominations.add(Nomination(
					awardId = nominationObject.get("award_id").asInt,
					contestId = nominationObject.get("contest_id").getField()?.asInt,
					contestName = nominationObject.get("contest_name").getField()?.asString,
					contestYear = nominationObject.get("contest_year").getField()?.asInt,
					cwId = nominationObject.get("cw_id").getField()?.asInt,
					cwIsWinner = nominationObject.get("cw_is_winner").getField()?.asInt == 1,
					cwPostfix = nominationObject.get("cw_postfix").getField()?.asString,
					cwPrefix = nominationObject.get("cw_prefix").getField()?.asString,
					inList = nominationObject.get("award_in_list").getField()?.asInt == 1,
					isOpened = nominationObject.get("award_is_opened").getField()?.asInt == 1,
					name = nominationObject.get("award_name").getField()?.asString,
					nominationId = nominationObject.get("nomination_id").getField()?.asInt,
					nominationName = nominationObject.get("nomination_name").getField()?.asString,
					nominationRusName = nominationObject.get("nomination_rusname").getField()?.asString,
					rusName = nominationObject.get("award_rusname").getField()?.asString,
					workId = nominationObject.get("work_id").getField()?.asInt,
					workName = nominationObject.get("work_name").getField()?.asString,
					workRusName = nominationObject.get("work_rusname").getField()?.asString,
					workYear = nominationObject.get("work_year").getField()?.asInt,
					position = it.index
			))
		}
	}

	private fun JsonObject.parseWorks(
			works: ArrayList<Work>,
			authors: HashSet<Author>,
			workAuthors: ArrayList<WorkAuthor>
	) {
		for ((key, value) in this.entrySet()) {
			value.asJsonObject
					.getAsJsonArray("list")
					.parseBlockWorks(
							works,
							authors,
							workAuthors,
							key.toInt()
					)
		}
	}

	private fun JsonArray.parseBlockWorks(
			works: ArrayList<Work>,
			authors: HashSet<Author>,
			workAuthors: ArrayList<WorkAuthor>,
			blockId: Int
	) {
		this.withIndex().forEach {
			val workObject = it.value.asJsonObject
			val authorArray = workObject.getAsJsonArray("authors")
			authorArray.withIndex().forEach {
				val authorObject = it.value.asJsonObject
				authors.add(Author(
						authorId = authorObject.get("id").asInt,
						name = authorObject.get("name").asString
				))
				workAuthors.add(WorkAuthor(
						workId = workObject.get("work_id").asInt,
						authorId = authorObject.get("id").asInt,
						role = authorObject.get("type").asString,
						position = it.index
				))
			}
			works.add(Work(
					workId = workObject.get("work_id").asInt,
					blockId = blockId,
					canDownload = workObject.get("public_download_file").getField()?.asInt == 1,
					description = workObject.get("work_description").getField()?.asString,
					hasLp = workObject.get("work_lp").getField()?.asInt == 1,
					midMark = workObject.get("val_midmark").getField()?.asFloat,
					midMarkByWeight = workObject.get("val_midmark_by_weight").getField()?.asFloat,
					name = workObject.get("work_name").getField()?.asString,
					nameAlt = workObject.get("work_name_alt").getField()?.asString,
					nameBonus = workObject.get("work_name_bonus").getField()?.asString,
					nameOrig = workObject.get("work_name_orig").getField()?.asString,
					notFinished = workObject.get("work_notfinished").getField()?.asInt == 1,
					preparing = workObject.get("work_preparing").getField()?.asInt == 1,
					published = workObject.get("work_published").getField()?.asInt == 1,
					publishStatus = workObject.get("publish_status").getField()?.asString,
					rating = workObject.get("val_rating").getField()?.asFloat,
					responseCount = workObject.get("val_responsecount").getField()?.asInt,
					rootWorkId = workObject.get("work_root_saga")?.asJsonObject?.get("work_id")?.asInt,
					type = workObject.get("work_type_id").getField()?.asInt,
					writeYear = workObject.get("work_year_of_write").getField()?.asInt,
					year = workObject.get("work_year").getField()?.asInt,
					voters = workObject.get("val_voters").getField()?.asInt,
					position = it.index
			))
		}
	}

	private fun JsonObject.parseChildWorks(
			childWorks: ArrayList<ChildWork>,
			authors: HashSet<Author>,
			workAuthors: ArrayList<WorkAuthor>
	) {
		for ((_, value) in this.entrySet()) {
			value.asJsonObject
					.getAsJsonArray("list")
					// fail
					.asJsonObject
					.getAsJsonArray("children")
					?.parseCycleChildWorks(
							childWorks,
							authors,
							workAuthors
					)
		}
	}

	private fun JsonArray.parseCycleChildWorks(
			childWorks: ArrayList<ChildWork>,
			authors: HashSet<Author>,
			workAuthors: ArrayList<WorkAuthor>
	) {
		this.withIndex().forEach {
			val workObject = it.value.asJsonObject
			val authorArray = workObject.getAsJsonArray("authors")
			authorArray.withIndex().forEach {
				val authorObject = it.value.asJsonObject
				authors.add(Author(
						authorId = authorObject.get("id").asInt,
						name = authorObject.get("name").asString
				))
				workAuthors.add(WorkAuthor(
						workId = workObject.get("work_id").asInt,
						authorId = authorObject.get("id").asInt,
						role = authorObject.get("type").asString,
						position = it.index
				))
			}
			childWorks.add(ChildWork(
					workId = workObject.get("work_id").asInt,
					canDownload = workObject.get("public_download_file").getField()?.asInt == 1,
					deep = workObject.get("deep").asInt,
					description = workObject.get("work_description").getField()?.asString,
					hasLp = workObject.get("work_lp").getField()?.asInt == 1,
					midMark = workObject.get("val_midmark").getField()?.asFloat,
					midMarkByWeight = workObject.get("val_midmark_by_weight").getField()?.asFloat,
					name = workObject.get("work_name").getField()?.asString,
					nameAlt = workObject.get("work_name_alt").getField()?.asString,
					nameBonus = workObject.get("work_name_bonus").getField()?.asString,
					nameOrig = workObject.get("work_name_orig").getField()?.asString,
					notFinished = workObject.get("work_notfinished").getField()?.asInt == 1,
					plus = workObject.get("plus").getField()?.asInt == 1,
					preparing = workObject.get("work_preparing").getField()?.asInt == 1,
					published = workObject.get("work_published").getField()?.asInt == 1,
					publishStatus = workObject.get("publish_status").getField()?.asString,
					rating = workObject.get("val_midmark_rating").getField()?.asFloat,
					responseCount = workObject.get("val_responsecount").getField()?.asInt,
					rootWorkId = workObject.get("work_root_saga")?.asJsonObject?.get("work_id")?.asInt,
					type = workObject.get("work_type_id").getField()?.asInt,
					writeYear = workObject.get("work_year_of_write").getField()?.asInt,
					year = workObject.get("work_year").getField()?.asInt,
					voters = workObject.get("val_voters").getField()?.asInt,
					position = it.index
			))
		}
	}

	override fun deserialize(
			json: JsonElement,
			typeOfT: Type,
			context: JsonDeserializationContext?
	): AuthorPageInfo {
		val jsonObject = json.asJsonObject

		val authors = hashSetOf<Author>()
		val laResume = arrayListOf<LaResume>()
		val pseudonyms = arrayListOf<Pseudonym>()
		val sites = arrayListOf<Site>()
		jsonObject.parseAuthor(authors, laResume, pseudonyms, sites)

		val nominations = arrayListOf<Nomination>()
		val awards = jsonObject.get("awards").asJsonObject
		awards.getAsJsonArray("nom")?.parseNominations(nominations)
		awards.getAsJsonArray("win")?.parseNominations(nominations)

		val works = arrayListOf<Work>()
		val childWorks = arrayListOf<ChildWork>()
		val workAuthors = arrayListOf<WorkAuthor>()
		val cyclesBlocks = jsonObject.getAsJsonObject("cycles_blocks")
		cyclesBlocks?.parseWorks(
				works,
				authors,
				workAuthors
		)
		cyclesBlocks?.parseChildWorks(
				childWorks,
				authors,
				workAuthors
		)
		val worksBlocks = jsonObject.getAsJsonObject("works_blocks")
		worksBlocks?.parseWorks(
				works,
				authors,
				workAuthors
		)

		return AuthorPageInfo(
				authors = authors,
				childWorks = childWorks,
				laResume = laResume,
				nominations = nominations,
				pseudonyms = pseudonyms,
				sites = sites,
				works = works,
				workAuthors = workAuthors
		)
	}
}
