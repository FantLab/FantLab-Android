package ru.fantlab.android.data.dao

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AttachModel(
		val filename: String,
		val filepath: String,
		var progress: Int = 0
) : Parcelable

