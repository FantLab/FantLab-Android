package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditionContent(
		val title : String,
		val level: Int
) : Parcelable
