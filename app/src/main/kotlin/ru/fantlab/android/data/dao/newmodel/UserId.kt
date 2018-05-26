package ru.fantlab.android.data.dao.newmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserId(
		@SerializedName("user_id") val id: Int
) : Parcelable