package org.odddev.fantlab.core.models

import android.support.annotation.Keep
import android.util.SparseArray
import java.util.*

@Keep
class Author(
		val id: Int,
		val anons: String,
		val biography: String,
		val notes: String,
		val biographySource: String,
		val biographySourceUrl: String,
		val birthday: Calendar?,
		val deathday: Calendar?,
		val name: String,
		val nameOrig: String,
		val nameRp: String,
		val nameShort: String,
		val sex: String,
		val countryId: Int,
		val countryName: String,
		val awards: List<Award>,
		val works: SparseArray<List<Work>>,
		val sites: List<String>, // a la "[link=${bio.sourceLink}]${bio.source}[/link]".formatText()
		val awardCount: Int,
		val editionCount: Int,
		val movieCount: Int,
		val markCount: Int,
		val responseCount: Int,
		val type: String,
		val compiler: String,
		val curator: Int,
		val fantastic: Int,
		val isFv: Boolean,
		val isOpened: Boolean) {

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

	class Work(
			val autors: List<String>, // a la "[link=https://fantlab.ru/autor$id]${name}[/link]".formatText()
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
