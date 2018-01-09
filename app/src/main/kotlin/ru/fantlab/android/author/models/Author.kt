package ru.fantlab.android.author.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "autors")
data class Author(

		@PrimaryKey
		@ColumnInfo(name = "autor_id")
		val authorId: Int,

		@ColumnInfo(name = "shortrusname")
		val shortRusName: String? = null,

		@ColumnInfo(name = "rusname")
		val rusName: String? = null,

		@ColumnInfo(name = "rusname_rp")
		val rusNameRp: String? = null,

		@ColumnInfo
		val name: String? = null,

		@ColumnInfo
		val sex: Boolean? = null,

		@ColumnInfo(name = "country_id")
		val countryId: Int? = null,

		// todo date/calendar/etc (datetime in fact)
		@ColumnInfo(name = "birthday")
		val birthDay: String? = null,

		// todo date/calendar/etc (datetime in fact)
		@ColumnInfo(name = "deathday")
		val deathDay: String? = null,

		@ColumnInfo(name = "homepage")
		val homePage: String? = null,

		@ColumnInfo
		val blog: String? = null,

		@ColumnInfo
		val anons: String? = null,

		@ColumnInfo
		val biography: String? = null,

		@ColumnInfo(name = "biography_comment")
		val biographyComment: String? = null,

		@ColumnInfo
		val source: String? = null,

		@ColumnInfo(name = "source_link")
		val sourceLink: String? = null,

		@ColumnInfo(name = "add_info")
		val addInfo: String? = null,

		@ColumnInfo(name = "is_fv")
		val isFv: Boolean? = null,

		@ColumnInfo(name = "is_opened")
		val isOpened: Boolean? = null,

		@ColumnInfo
		val curator: Int? = null,

		@ColumnInfo
		val compiler: String? = null,

		@ColumnInfo
		val fantastic: Int? = null,

		// todo date/calendar/etc (datetime in fact)
		@ColumnInfo(name = "date_change")
		val lastModified: String? = null
)