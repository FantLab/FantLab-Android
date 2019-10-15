package ru.fantlab.android.data.db.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search")
data class Search(
		@PrimaryKey
		@ColumnInfo(name = "search_query")
		val query: String,

		@ColumnInfo(name = "user_id")
		val userId: Int
)