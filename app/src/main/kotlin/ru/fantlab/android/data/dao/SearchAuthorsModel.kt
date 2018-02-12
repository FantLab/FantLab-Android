package ru.fantlab.android.data.dao

import com.google.gson.annotations.SerializedName

data class SearchAuthorsModel(
		@SerializedName("autor_id")
		var authorId: Int,
		@SerializedName("birthyear")
		var birthYear: Int,
		var country: String,
		var countryId: Int,
		@SerializedName("deathyear")
		var deathYear: Int,
		@SerializedName("editioncount")
		var editionCount: Int,
		var isOpened: Int,
		@SerializedName("markcount")
		var markCount: Int,
		@SerializedName("midmark")
		var midMark: Int,
		@SerializedName("moviecount")
		var movieCount: Int,
		var name: String,
		var pseudoNames: String,
		@SerializedName("responsecount")
		var responseCount: Int,
		@SerializedName("rusname")
		var rusName: String
)