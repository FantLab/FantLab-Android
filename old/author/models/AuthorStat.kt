package ru.fantlab.android.old.author.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
		tableName = "autor_stats",
		indices = [Index(value = ["autor_id"], unique = true)]
)
data class AuthorStat(

		@PrimaryKey(autoGenerate = true)
		@ColumnInfo
		val id: Int? = null,

		@ColumnInfo(name = "autor_id")
		val authorId: Int,

		@ColumnInfo(name = "midmark")
		val midMark: Int? = null,

		@ColumnInfo
		val marking: Int? = null,

		@ColumnInfo(name = "markcount")
		val markCount: Int? = null,

		@ColumnInfo(name = "usercount")
		val userCount: Int? = null,

		@ColumnInfo(name = "responsecount")
		val responseCount: Int? = null,

		@ColumnInfo(name = "editioncount")
		val editionCount: Int? = null,

		@ColumnInfo(name = "moviecount")
		val movieCount: Int? = null,

		@ColumnInfo(name = "awardcount")
		val awardCount: Int? = null,

		@ColumnInfo(name = "nominationcount")
		val nominationCount: Int? = null
)