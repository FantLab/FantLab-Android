package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class Films(
		@SerializedName("screen_version") val screenVersions: ArrayList<Film>,
		@SerializedName("novelization") val novelizations: ArrayList<Film>,
		@SerializedName("screenplay") val screenplays: ArrayList<Film>
) {
	data class Film(
			val country: String,
			val director: String,
			@SerializedName("film_id") val id: Int,
			val name: String,
			@SerializedName("rusname") val rusName: String,
			val type: Int,
			val year: Int
	)
}