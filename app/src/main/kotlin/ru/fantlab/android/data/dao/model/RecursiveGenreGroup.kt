package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class RecursiveGenreGroup(
		val genre: ArrayList<Genre>,
		@SerializedName("genre_group_id") val genreGroupId: Int,
		val label: String
) : Parcelable {
	@Keep
	@Parcelize
	data class Genre(
			val genre: ArrayList<Genre>?,
			@SerializedName("genre_id") val genreId: Int,
			val label: String,
			val percent: Float
	) : Parcelable
}