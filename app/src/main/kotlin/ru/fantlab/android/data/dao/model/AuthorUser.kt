package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class AuthorUser(
		@SerializedName("registered_user_id") val id: Int,
		@SerializedName("registered_user_login") val login: String,
		@SerializedName("registered_user_sex") val sex: Int
) : Parcelable