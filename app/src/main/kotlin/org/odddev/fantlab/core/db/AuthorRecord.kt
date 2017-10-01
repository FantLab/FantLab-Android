package org.odddev.fantlab.core.db

import android.os.Parcelable
import android.support.annotation.Keep
import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.Persistable
import java.util.*

@Keep
@Entity
interface AuthorRecord : Parcelable, Persistable {

	@get:Key
	@get:Generated
	val id: Int

	var authorId: Int

	var anons: String?

	var biography: String?

	var biographyNotes: String?

	var biographySource: String?

	var biographySourceUrl: String?

	//var birthDay: Calendar?

	var compiler: String?

	var countryId: Int?

	var countryName: String?

	var curator: Int?

	//var deathDay: Calendar?

	var fantastic: Int?

	var isOpened: Boolean?

	//var lastModified: Calendar?

	var name: String?

	var nameOrig: String?

	var nameRp: String?

	var nameShort: String?

	var registeredUserId: Int?

	var registeredUserLogin: String?

	var registeredUserSex: Int?

	var sex: String?

	var statAwardCount: Int?

	var statEditionCount: Int?

	var statMarkCount: Int?

	var statMovieCount: Int?

	var statResponseCount: Int?

	var statWorkCount: Int?
}