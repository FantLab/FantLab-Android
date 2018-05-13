package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class GenreGroup(
		val genre: ArrayList<Genre>,
		@SerializedName("genre_group_id") val genreGroupId: Int,
		val label: String
) {
	data class Genre(
			val genre: ArrayList<Genre>?,
			@SerializedName("genre_id") val genreId: Int,
			val label: String
	)
}