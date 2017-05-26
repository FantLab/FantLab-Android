package org.odddev.fantlab.autors.autor

import java.util.*

/**
 * Created by kefir on 28.01.2017.
 */
class AutorFull(
		val anons: String,
		val awards: List<Award>,
		val biography: String,
		val biographyNotes: String,
		val birthday: Calendar,
		val compiler: String,
		val countryId: Int,
		val countryName: String,
		val curator: Int,
		val fantastic: Int,
		val id: Int,
		val isFv: Boolean,
		val isOpened: Boolean,
		val name: String,
		val nameOrig: String,
		val nameRp: String,
		val nameShort: String,
		val sex: String,
		val sites: List<Site>,
		val source: String,
		val sourceLink: String,
		val stat: Stat,
		val type: String) {

	fun getImageLink(): String = "/autors/$id"

	fun getImagePreviewLink(): String = "/autors/small/$id"

	class Site(
			val description: String,
			val site: String
	)

	class Stat(
			val award: Int,
			val edition: Int,
			val movie: Int,
			val mark: Int,
			val response: Int
	)

	class Award(
			val id: Int,
			val inList: Boolean,
			val isOpened: Boolean,
			val name: String,
			val rusName: String,
			val contestId: Int,
			val contestName: String,
			val contestYear: Int,
			val cwId: Int,
			val cwIsWinner: Boolean,
			val cwPostfix: String,
			val cwPrefix: String,
			val nominationId: Int,
			val nominationName: String,
			val nominationRusname: String,
			val workId: Int,
			val workName: String,
			val workRusname: String,
			val workYear: Int
	)
}
