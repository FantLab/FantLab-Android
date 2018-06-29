package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClassificationGenre(
		@SerializedName("label") val label: String,
        @SerializedName("genre_id") val genreId: Int,
        @SerializedName("genre_level") val genreLevel: Int
        )  : Parcelable