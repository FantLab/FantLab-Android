package org.odddev.fantlab.authors

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "autors")
data class Author(

		@PrimaryKey
		@ColumnInfo(name = "autor_id")
		val authorId: Int,

		@ColumnInfo(name = "shortrusname")
		val shortRusName: String?,

		@ColumnInfo(name = "rusname")
		val rusName: String?,

		@ColumnInfo(name = "rusname_rp")
		val rusNameRp: String?,

		@ColumnInfo
		val name: String?,

		@ColumnInfo
		val sex: Boolean?,

		@ColumnInfo(name = "country_id")
		val countryId: Int?,

		// todo date/calendar/etc (datetime in fact)
		@ColumnInfo(name = "birthday")
		val birthDay: String?,

		// todo date/calendar/etc (datetime in fact)
		@ColumnInfo(name = "deathday")
		val deathDay: String?,

		@ColumnInfo(name = "homepage")
		val homePage: String?,

		@ColumnInfo
		val blog: String?,

		@ColumnInfo
		val anons: String?,

		@ColumnInfo
		val biography: String?,

		@ColumnInfo(name = "biography_comment")
		val biographyComment: String?,

		@ColumnInfo
		val source: String?,

		@ColumnInfo(name = "source_link")
		val sourceLink: String?,

		@ColumnInfo(name = "add_info")
		val addInfo: String?,

		@ColumnInfo(name = "is_fv")
		val isFv: Boolean?,

		@ColumnInfo(name = "is_opened")
		val isOpened: Boolean?,

		@ColumnInfo
		val curator: Int?,

		@ColumnInfo
		val compiler: String?,

		@ColumnInfo
		val fantastic: Int?,

		// todo date/calendar/etc (datetime in fact)
		@ColumnInfo(name = "date_change")
		val lastModified: String?
)