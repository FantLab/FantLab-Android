package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class ClassificationGenre(
		val label: String,
		val genreId: Int,
		val genreLevel: Int
) : Parcelable