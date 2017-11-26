package org.odddev.fantlab.authors

import android.arch.persistence.room.ColumnInfo

data class AuthorInList(

		@ColumnInfo(name = "autor_id")
		val authorId: Int,

		@ColumnInfo(name = "shortrusname")
		val shortRusName: String? = null,

		@ColumnInfo(name = "rusname")
		val rusName: String? = null,

		@ColumnInfo
		val name: String? = null
) : IAuthorItem