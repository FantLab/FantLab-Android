package ru.fantlab.android.data.db.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "response")
data class Response(
		@PrimaryKey
		val url: String,

		@ColumnInfo
		val userId: Int,

		@ColumnInfo
		val response: String,

		@ColumnInfo(name = "api_version")
		val apiVersion: String,

		@ColumnInfo(name = "timestamp")
		val timeStamp: Long
)