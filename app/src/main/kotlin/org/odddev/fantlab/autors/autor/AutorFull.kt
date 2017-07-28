package org.odddev.fantlab.autors.autor

import android.util.SparseArray
import android.util.SparseIntArray
import java.util.*

class AutorFull(
		val id: Int,
		val biography: Biography,
		val country: Country,
		val awards: List<Award>,
		val works: SparseArray<List<Work>>,
		val sites: List<Site>,
		val stat: Stat,
		val type: String,
		val compiler: String,
		val curator: Int,
		val fantastic: Int,
		val isFv: Boolean,
		val isOpened: Boolean,
		val myMarks: SparseIntArray,
		val myResponses: List<Int>) {

	fun getImageLink(): String = "/autors/$id"

	fun getImagePreviewLink(): String = "/autors/small/$id"

	class Biography(
			val anons: String,
			val text: String,
			val notes: String,
			val source: String,
			val sourceLink: String,
			val birthday: Calendar?,
			val deathday: Calendar?,
			val name: String,
			val nameOrig: String,
			val nameRp: String,
			val nameShort: String,
			val sex: String
	)

	class Country(
			val id: Int,
			val name: String
	)

	class Site(
			val description: String,
			val site: String
	)

	class Stat(
			val award: Int = -1,
			val edition: Int = -1,
			val movie: Int = -1,
			val mark: Int = -1,
			val response: Int = -1
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

	class AutorLink(
			val id: Int,
			val name: String
	)

	class Work(
			val autors: List<AutorLink>,
			val midmark: Float,
			val rating: Float,
			val responseCount: Int,
			val voters: Int,
			val description: String,
			val id: Int,
			val name: String,
			val nameAlt: String,
			val nameOrig: String,
			val nameBonus: String,
			val notFinished: Boolean,
			val published: Boolean,
			val preparing: Boolean, // в планах
			val type: Int, // work_type_id
			val year: Int,
			val writeYear: Int,
			val deep: Int,
			val plus: Boolean,
			val canDownload: Boolean,
			val hasLp: Boolean, // есть лингвопрофиль
			val forChildren: Boolean
	)
}
