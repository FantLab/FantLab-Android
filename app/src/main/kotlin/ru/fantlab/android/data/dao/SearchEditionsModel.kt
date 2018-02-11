package ru.fantlab.android.data.dao

import com.google.gson.annotations.SerializedName

data class SearchEditionsModel(
		@SerializedName("autors")
		val authors: String,
		val comment: String,
		val compilers: String,
		val correct: Int,
		val editionId: Int,
		val isbn1: String,
		val isbn2: String,
		val name: String,
		val notes: String,
		val planDate: Any, // what real type?
		val publisher: String,
		val series: String,
		val year: Int
)