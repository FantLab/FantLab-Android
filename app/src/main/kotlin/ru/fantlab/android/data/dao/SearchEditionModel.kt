package ru.fantlab.android.data.dao

import com.google.gson.annotations.SerializedName

data class SearchEditionModel(
		@SerializedName("autors")
		var authors: String,
		var comment: String,
		var compilers: String,
		var correct: Int,
		var editionId: Int,
		var isbn1: String,
		var isbn2: String,
		var name: String,
		var notes: String,
		var planDate: Any, // what real type?
		var publisher: String,
		var series: String,
		var year: String
)