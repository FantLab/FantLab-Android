package ru.fantlab.android.data.dao.model
import com.google.gson.annotations.SerializedName


data class Login(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)