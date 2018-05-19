package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class AuthorUser(
		@SerializedName("registered_user_id") val id: Int,
		@SerializedName("registered_user_login") val login: String,
		@SerializedName("registered_user_sex") val sex: Int
)