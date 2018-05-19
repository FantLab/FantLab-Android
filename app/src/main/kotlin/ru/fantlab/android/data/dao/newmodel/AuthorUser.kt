package ru.fantlab.android.data.dao.newmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthorUser(
		@SerializedName("registered_user_id") val id: Int,
		@SerializedName("registered_user_login") val login: String,
		@SerializedName("registered_user_sex") val sex: Int
) : Parcelable