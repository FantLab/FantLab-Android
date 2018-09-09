package ru.fantlab.android.data.db.response

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "search")
data class Search(
		@PrimaryKey
		@ColumnInfo(name = "search_query")
		val query: String,

		@ColumnInfo(name = "user_id")
		val userId: Int
)