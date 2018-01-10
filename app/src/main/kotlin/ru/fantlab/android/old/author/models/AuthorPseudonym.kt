package ru.fantlab.android.old.author.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

// todo с val не компилируется (Cannot find setter for field). почему?
@Entity(tableName = "autor_pseudonyms")
data class AuthorPseudonym(

		@PrimaryKey(autoGenerate = true)
		@ColumnInfo
		var id: Int? = null,

		@ColumnInfo(name = "autor_id")
		var authorId: Int? = null,

		@ColumnInfo(name = "shortrusname")
		var shortRusName: String? = null,

		@ColumnInfo(name = "rusname")
		var rusName: String? = null,

		@ColumnInfo(name = "rusname_rp")
		var rusNameRp: String? = null,

		@ColumnInfo
		var name: String? = null,

		@ColumnInfo(name = "is_real")
		var isReal: Boolean? = null
)