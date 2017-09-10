package org.odddev.fantlab.core.models

import android.support.annotation.Keep
import android.util.SparseArray
import java.util.*

@Keep
data class Author(
		val id: Int,
		val anons: String? = null,
		val biography: String? = null,
		val notes: String? = null,
		val biographySource: String? = null,
		val biographySourceUrl: String? = null,
		val birthday: Calendar? = null,
		val deathday: Calendar? = null,
		val name: String? = null,
		val nameOrig: String? = null,
		val nameRp: String? = null,
		val nameShort: String? = null,
		val sex: String? = null,
		val countryId: Int? = null,
		val countryName: String? = null,
		val sites: List<String>? = null, // a la "[link=${bio.sourceLink}]${bio.source}[/link]".formatText()
		val awardCount: Int? = null,
		val editionCount: Int? = null,
		val movieCount: Int? = null,
		val markCount: Int? = null,
		val responseCount: Int? = null,
		val type: String? = null,
		val compiler: String? = null,
		val curator: Int? = null,
		val fantastic: Int? = null,
		val isFv: Boolean? = null,
		val isOpened: Boolean? = null
)
