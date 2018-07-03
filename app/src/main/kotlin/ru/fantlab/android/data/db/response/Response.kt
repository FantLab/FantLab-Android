package ru.fantlab.android.data.db.response

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "response")
data class Response(
		@PrimaryKey
		val url: String,

		@ColumnInfo(name = "api_version")
		val apiVersion: String,

		@ColumnInfo
		val response: String
)