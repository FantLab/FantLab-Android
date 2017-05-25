package org.odddev.fantlab.autors.autor

import java.util.*

/**
 * Created by kefir on 28.01.2017.
 */
class AutorFull(
		val anons: String,
		val autorId: Int,
		val biography: String,
		val biographyNotes: String,
		val birthday: Calendar,
		val compiler: String,
		val countryId: Int,
		val countryName: String,
		val curator: Int,
		val fantastic: Int,
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

	fun getImageLink(): String = "/autors/$autorId"

	fun getImagePreviewLink(): String = "/autors/small/$autorId"

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
}
