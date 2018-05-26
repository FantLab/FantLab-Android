package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Films(
		@SerializedName("screen_version") val screenVersions: ArrayList<Film>,
		@SerializedName("novelization") val novelizations: ArrayList<Film>,
		@SerializedName("screenplay") val screenplays: ArrayList<Film>
) : Parcelable {
	@Parcelize
	data class Film(
			val country: String,
			val director: String,
			@SerializedName("film_id") val id: Int,
			val name: String,
			@SerializedName("rusname") val rusName: String,
			val type: Int,
			val year: Int
	) : Parcelable
}