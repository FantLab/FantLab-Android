package ru.fantlab.android.data.dao

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppLanguageModel(
		val value: String,
		val label: String
) : Parcelable