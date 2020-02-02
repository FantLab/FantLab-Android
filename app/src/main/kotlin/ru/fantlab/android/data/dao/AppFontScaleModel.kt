package ru.fantlab.android.data.dao

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppFontScaleModel(
		val value: Float,
		val label: String
) : Parcelable