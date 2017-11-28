package org.odddev.fantlab.author

import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import org.odddev.fantlab.author.models.Author as DetailAuthor

@Keep
data class AuthorResponse(
		val anons: String? = null,
		@SerializedName("autor_id")
		val authorId: Int,
		val awards: Awards? = null,
		val biography: String? = null,
		@SerializedName("biography_notes")
		val biographyNotes: String? = null,
		@SerializedName("birthday")
		val birthDay: String? = null, // todo datetime на самом деле
		val compiler: String? = null,
		@SerializedName("country_id")
		val countryId: Int? = null,
		@SerializedName("country_name")
		val countryName: String? = null,
		val curator: Int? = null,
		val cycles: Any? = null, // todo какого типа на самом деле?
		@SerializedName("cycles_blocks")
		val cyclesBlocks: CyclesBlocks? = null,
		@SerializedName("deathday")
		val deathDay: String? = null, // todo datetime на самом деле
		val fantastic: Int? = null,
		@SerializedName("fl_blog_anons")
		val flBlogAnons: Any? = null, // todo какого типа на самом деле?
		@SerializedName("is_opened")
		val isOpened: Int? = null,
		@SerializedName("la_resume")
		val laResume: List<Any>? = null,
		@SerializedName("last_modified")
		val lastModified: String? = null, // todo datetime на самом деле
		val name: String? = null,
		@SerializedName("name_orig")
		val nameOrig: String? = null,
		@SerializedName("name_pseudonyms")
		val pseudonyms: List<Pseudonym>? = null,
		@SerializedName("name_rp")
		val nameRp: String? = null,
		@SerializedName("name_short")
		val nameShort: String? = null,
		@SerializedName("registered_user_id")
		val registeredUserId: Any? = null, // todo какого типа на самом деле?
		@SerializedName("registered_user_login")
		val registeredUserLogin: Any? = null, // todo какого типа на самом деле?
		@SerializedName("registered_user_sex")
		val registeredUserSex: Any? = null, // todo какого типа на самом деле?
		val sex: String? = null,
		val sites: List<Site>? = null,
		val source: String? = null,
		@SerializedName("source_link")
		val sourceLink: String? = null,
		val stat: Stat? = null,
		val works: Any? = null, // todo какого типа на самом деле?
		@SerializedName("works_blocks")
		val worksBlocks: WorksBlocks
) {
	@Keep
	data class Author(
			val id: Int,
			val name: String? = null,
			val type: String? = null
	)

	@Keep
	data class Awards(
			@SerializedName("nom")
			val nominations: List<Nomination>? = null,
			@SerializedName("win")
			val wins: List<Nomination>? = null
	)

	@Keep
	data class Block(
			val id: Int,
			val list: List<Work>,
			val name: String,
			val title: String
	)

	@Keep
	data class CyclesBlocks(
			// Циклы произведений
			@SerializedName("1")
			val cycle: Block? = null,
			// Сериалы
			@SerializedName("2")
			val serial: Block? = null,
			// Романы-эпопеи
			@SerializedName("3")
			val epic: Block? = null,
			// Условные циклы
			@SerializedName("4")
			val conditionalCycle: Block? = null
	)

	@Keep
	data class Nomination(
			@SerializedName("award_icon")
			val awardIcon: String? = null,
			@SerializedName("award_id")
			val awardId: Int? = null,
			@SerializedName("award_in_list")
			val awardInList: Int? = null,
			@SerializedName("award_is_opened")
			val awardIsOpened: Int? = null,
			@SerializedName("award_name")
			val awardName: String? = null,
			@SerializedName("award_rusname")
			val awardRusName: String? = null,
			@SerializedName("contest_id")
			val contestId: Int? = null,
			@SerializedName("contest_name")
			val contestName: String? = null,
			@SerializedName("contest_year")
			val contestYear: Int? = null,
			@SerializedName("cw_id")
			val cwId: Int? = null,
			@SerializedName("cw_is_winner")
			val cwIsWinner: Int? = null,
			@SerializedName("cw_postfix")
			val cwPostfix: String? = null,
			@SerializedName("cw_prefix")
			val cwPrefix: String? = null,
			@SerializedName("nomination_id")
			val nominationId: Int? = null,
			@SerializedName("nomination_name")
			val nominationName: String? = null,
			@SerializedName("nomination_rusname")
			val nominationRusname: String? = null,
			@SerializedName("work_id")
			val workId: Int? = null,
			@SerializedName("work_name")
			val workName: String? = null,
			@SerializedName("work_rusname")
			val workRusName: String? = null,
			@SerializedName("work_year")
			val workYear: String? = null // todo почему вместо числа может возвращаться пустая строка? О_о
	)

	@Keep
	data class Pseudonym(
			@SerializedName("is_real")
			val isReal: Int? = null,
			val name: String? = null,
			@SerializedName("name_orig")
			val nameOrig: String? = null
	)

	@Keep
	data class Site(
			@SerializedName("descr")
			val description: String? = null,
			val site: String? = null
	)

	@Keep
	data class Stat(
			@SerializedName("awardcount")
			val awardCount: Int? = null,
			@SerializedName("editioncount")
			val editionCount: Int? = null,
			@SerializedName("markcount")
			val markCount: Int? = null,
			@SerializedName("moviecount")
			val movieCount: Int? = null,
			@SerializedName("responsecount")
			val responseCount: Int? = null,
			@SerializedName("workcount")
			val workCount: Int? = null
	)

	@Keep
	data class Work(
			val authors: List<Author>? = null,
			val children: List<Work>? = null,
			val deep: Int? = null,
			val plus: Int? = null,
			@SerializedName("position_index")
			val positionIndex: Int? = null,
			@SerializedName("position_is_node")
			val positionIsNode: Int? = null,
			@SerializedName("position_level")
			val positionLevel: Int? = null,
			@SerializedName("position_show_in_biblio")
			val positionShowInBiblio: Int? = null,
			@SerializedName("position_show_subworks_in_biblio")
			val positionShowSubworksInBiblio: Int? = null,
			@SerializedName("public_download_file")
			val publicDownloadFile: Int? = null,
			@SerializedName("publish_status")
			val publishStatus: String? = null,
			@SerializedName("val_midmark")
			val valMidMark: Float? = null,
			@SerializedName("val_midmark_by_weight")
			val valMidMarkByWeight: Float? = null,
			@SerializedName("val_midmark_rating")
			val valMidMarkRating: Float? = null,
			@SerializedName("val_rating")
			val valRating: Float? = null,
			@SerializedName("val_responsecount")
			val valResponseCount: Int? = null,
			@SerializedName("val_voters")
			val valVoters: Int? = null,
			@SerializedName("work_description")
			val workDescription: String? = null,
			@SerializedName("work_id")
			val workId: Int? = null,
			@SerializedName("work_lp")
			val workLp: Int? = null,
			@SerializedName("work_name")
			val workName: String? = null,
			@SerializedName("work_name_alt")
			val workNameAlt: String? = null,
			@SerializedName("work_name_bonus")
			val workNameBonus: String? = null,
			@SerializedName("work_name_orig")
			val workNameOrig: String? = null,
			@SerializedName("work_notfinished")
			val workNotFinished: Int? = null,
			@SerializedName("work_preparing")
			val workPreparing: Int? = null,
			@SerializedName("work_published")
			val workPublished: Int? = null,
			@SerializedName("work_root_saga")
			val workRootSaga: List<WorkRootSaga>? = null,
			@SerializedName("work_type")
			val workType: String? = null,
			@SerializedName("work_type_icon")
			val workTypeIcon: String? = null,
			@SerializedName("work_type_id")
			val workTypeId: Int? = null,
			@SerializedName("work_type_name")
			val workTypeName: String? = null,
			@SerializedName("work_year")
			val workYear: Int? = null,
			@SerializedName("work_year_of_write")
			val workYearOfWrite: Int? = null
	)

	@Keep
	data class WorksBlocks(
			// Романы
			@SerializedName("6")
			val novel: Block? = null,
			// Повести
			@SerializedName("7")
			val story: Block? = null,
			// Рассказы
			@SerializedName("8")
			val shortStory: Block? = null,
			// Повести и рассказы
			@SerializedName("9")
			val storyShortStory: Block? = null,
			// Микрорассказы
			@SerializedName("10")
			val microStory: Block? = null,
			// Сказки
			@SerializedName("12")
			val tale: Block? = null,
			// Документальные произведения
			@SerializedName("19")
			val documental: Block? = null,
			// Поэзия
			@SerializedName("22")
			val poem: Block? = null,
			// Пьесы
			@SerializedName("23")
			val piece: Block? = null,
			// Киносценарии
			@SerializedName("24")
			val scenario: Block? = null,
			// Графические произведения
			@SerializedName("25")
			val graphicNovel: Block? = null,
			// Диссертации
			@SerializedName("28")
			val disser: Block? = null,
			// Монографии
			@SerializedName("29")
			val monography: Block? = null,
			// Учебные издания
			@SerializedName("30")
			val study: Block? = null,
			// Статьи
			@SerializedName("31")
			val article: Block? = null,
			// Эссе
			@SerializedName("32")
			val essay: Block? = null,
			// Очерки
			@SerializedName("33")
			val sketch: Block? = null,
			// Репортажи
			@SerializedName("34")
			val reportage: Block? = null,
			// Энциклопедии и справочники
			@SerializedName("43")
			val encyclopedy: Block? = null,
			// Сборники
			@SerializedName("45")
			val collection: Block? = null,
			// Отрывки
			@SerializedName("49")
			val excerpt: Block? = null,
			// Рецензии
			@SerializedName("52")
			val review: Block? = null,
			// Интервью
			@SerializedName("53")
			val interview: Block? = null,
			// Антологии
			@SerializedName("54")
			val antology: Block? = null,
			// Журналы
			@SerializedName("55")
			val magazine: Block? = null,
			// Прочие произведения
			@SerializedName("57")
			val other: Block? = null,
			// Незаконченные произведения
			@SerializedName("100")
			val notFinished: Block? = null
	)

	@Keep
	data class WorkRootSaga(
			val prefix: String? = null,
			@SerializedName("work_id")
			val workId: Int? = null,
			@SerializedName("work_name")
			val workName: String? = null,
			@SerializedName("work_type")
			val workType: String? = null,
			@SerializedName("work_type_id")
			val workTypeId: Int? = null,
			@SerializedName("work_type_in")
			val workTypeIn: String? = null
	)

	fun getWorkAuthors(): HashMap<Int, DetailAuthor> {
		val authors = HashMap<Int, DetailAuthor>()
		cyclesBlocks?.cycle?.getAuthors(authors)
		cyclesBlocks?.serial?.getAuthors(authors)
		cyclesBlocks?.epic?.getAuthors(authors)
		cyclesBlocks?.conditionalCycle?.getAuthors(authors)
		worksBlocks.novel?.getAuthors(authors)
		worksBlocks.story?.getAuthors(authors)
		worksBlocks.shortStory?.getAuthors(authors)
		worksBlocks.storyShortStory?.getAuthors(authors)
		worksBlocks.microStory?.getAuthors(authors)
		worksBlocks.tale?.getAuthors(authors)
		worksBlocks.documental?.getAuthors(authors)
		worksBlocks.poem?.getAuthors(authors)
		worksBlocks.piece?.getAuthors(authors)
		worksBlocks.scenario?.getAuthors(authors)
		worksBlocks.graphicNovel?.getAuthors(authors)
		worksBlocks.disser?.getAuthors(authors)
		worksBlocks.monography?.getAuthors(authors)
		worksBlocks.study?.getAuthors(authors)
		worksBlocks.article?.getAuthors(authors)
		worksBlocks.essay?.getAuthors(authors)
		worksBlocks.sketch?.getAuthors(authors)
		worksBlocks.reportage?.getAuthors(authors)
		worksBlocks.encyclopedy?.getAuthors(authors)
		worksBlocks.collection?.getAuthors(authors)
		worksBlocks.excerpt?.getAuthors(authors)
		worksBlocks.review?.getAuthors(authors)
		worksBlocks.interview?.getAuthors(authors)
		worksBlocks.antology?.getAuthors(authors)
		worksBlocks.magazine?.getAuthors(authors)
		worksBlocks.other?.getAuthors(authors)
		worksBlocks.notFinished?.getAuthors(authors)
		return authors
	}

	private fun Block.getAuthors(authors: HashMap<Int, DetailAuthor>) =
			this.list.map {
				it.authors?.map {
					if (it.type == "autor") {
						authors.put(it.id, DetailAuthor(authorId = it.id, rusName = it.name))
					}
				}
			}
}