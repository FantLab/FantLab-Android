package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClassificationGenre(
		val label: String,
		val genreId: Int,
		val genreLevel: Int
) : Parcelable