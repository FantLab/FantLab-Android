package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Bookcase (
    val id: Int,
    val name: String,
    val description: String,
    val type: String
) : Parcelable