package org.odddev.fantlab.author.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "autor_pseudonyms")
data class AuthorPseudonym(

		@PrimaryKey(autoGenerate = true)
		@ColumnInfo
		val id: Int,

		@ColumnInfo(name = "autor_id")
		val authorId: Int?,

		@ColumnInfo(name = "shortrusname")
		val shortRusName: String?,

		@ColumnInfo(name = "rusname")
		val rusName: String?,

		@ColumnInfo(name = "rusname_rp")
		val rusNameRp: String?,

		@ColumnInfo
		val name: String?,

		@ColumnInfo(name = "is_real")
		val isReal: Boolean
)