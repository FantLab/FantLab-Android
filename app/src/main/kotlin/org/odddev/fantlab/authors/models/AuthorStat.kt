package org.odddev.fantlab.authors.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
		tableName = "autor_stats",
		indices = arrayOf(Index(value = "autor_id", unique = true))
)
data class AuthorStat(

		@PrimaryKey(autoGenerate = true)
		@ColumnInfo
		val id: Int,

		@ColumnInfo(name = "autor_id")
		val authorId: Int,

		@ColumnInfo(name = "midmark")
		val midMark: Int?,

		@ColumnInfo
		val marking: Int?,

		@ColumnInfo(name = "markcount")
		val markCount: Int,

		@ColumnInfo(name = "usercount")
		val userCount: Int?,

		@ColumnInfo(name = "responsecount")
		val responseCount: Int,

		@ColumnInfo(name = "editioncount")
		val editionCount: Int,

		@ColumnInfo(name = "moviecount")
		val movieCount: Int,

		@ColumnInfo(name = "awardcount")
		val awardCount: Int,

		@ColumnInfo(name = "nominationcount")
		val nominationCount: Int
)