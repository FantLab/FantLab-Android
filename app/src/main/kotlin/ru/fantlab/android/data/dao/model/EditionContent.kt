package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class EditionContent(
		val title: String,
		val level: Int
) : Parcelable
