package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GenreGroup(
		val genres: ArrayList<Pair<Int, Genre>>,
		@SerializedName("genre_group_id") val genreGroupId: Int,
		val label: String
) : Parcelable {
	@Parcelize
	data class Genre(
			@SerializedName("genre_id") val genreId: Int,
			val label: String,
			val percent: Float
	) : Parcelable
}