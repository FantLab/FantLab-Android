package ru.fantlab.android.data.dao.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("context")
    val context: String?
)