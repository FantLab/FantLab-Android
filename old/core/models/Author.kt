package ru.fantlab.android.old.core.models

import android.support.annotation.Keep
import java.util.*

@Keep
data class Author(
		// autor_id
		val authorId: Int,
		// anons
		val anons: String? = null,
		// biography
		val biography: String? = null,
		// biography_notes
		val biographyNotes: String? = null,
		// source
		val biographySource: String? = null,
		// source_link
		val biographySourceUrl: String? = null,
		// birthday
		val birthDay: Calendar? = null,
		// compiler
		val compiler: String? = null,
		// country_id
		val countryId: Int? = null,
		// country_name
		val countryName: String? = null,
		// curator
		val curator: Int? = null,
		// deathday
		val deathDay: Calendar? = null,
		// fantastic
		val fantastic: Int? = null,
		// fl_blog_anons
		// [что за параметр?]
		//val flBlogAnons: String? = null,
		// [есть только в /authors]
		//val isFv: Boolean? = null,
		// is_opened
		val isOpened: Boolean? = null,
		// last_modified
		val lastModified: Calendar? = null,
		// name
		val name: String? = null,
		// name_orig
		val nameOrig: String? = null,
		// name_rp
		val nameRp: String? = null,
		// name_short
		val nameShort: String? = null,
		// registered_user_id
		val registeredUserId: Int? = null,
		// registered_user_login
		val registeredUserLogin: String? = null,
		// registered_user_sex
		val registeredUserSex: Int? = null,
		// sex
		val sex: String? = null,
		// stat -> awardcount
		val statAwardCount: Int? = null,
		// stat -> editioncount
		val statEditionCount: Int? = null,
		// stat -> markcount
		val statMarkCount: Int? = null,
		// stat -> moviecount
		val statMovieCount: Int? = null,
		// stat -> responsecount
		val statResponseCount: Int? = null,
		// stat -> workcount
		val statWorkCount: Int? = null
)
