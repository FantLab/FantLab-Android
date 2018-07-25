package ru.fantlab.android.data.dao.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Smile(
		val id : String,
		val description : String
) : Parcelable