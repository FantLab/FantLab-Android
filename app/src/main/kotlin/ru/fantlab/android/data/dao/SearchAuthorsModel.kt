package ru.fantlab.android.data.dao

import com.google.gson.annotations.SerializedName

data class SearchAuthorsModel(
		@SerializedName("autor_id")
		val authorId: Int,
		@SerializedName("birthyear")
		val birthYear: Int,
		val country: String,
		val countryId: Int,
		@SerializedName("deathyear")
		val deathYear: Int,
		@SerializedName("editioncount")
		val editionCount: Int,
		val isOpened: Boolean,
		@SerializedName("markcount")
		val markCount: Int,
		@SerializedName("midmark")
		val midMark: Int,
		@SerializedName("moviecount")
		val movieCount: Int,
		val name: String,
		val pseudoNames: String,
		@SerializedName("responsecount")
		val responseCount: Int,
		@SerializedName("rusname")
		val rusName: String
)