package ru.fantlab.android.data.dao.newmodel

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class GenreGroup(
		val genre: List<Genre>,
		@SerializedName("genre_group_id") val genreGroupId: Int,
		val label: String
) {
	data class Genre(
			val genre: List<Genre>?,
			@SerializedName("genre_id") val genreId: Int,
			val label: String
	)

	class Deserializer : ResponseDeserializable<GenreGroup> {
		override fun deserialize(content: String): GenreGroup {
			return Gson().fromJson(content, GenreGroup::class.java)
		}
	}
}