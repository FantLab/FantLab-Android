package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class AuthorCard(
        val type: String,
        val id: Int,
        val title: String,
        val description: String,
        val image: String,
        @SerializedName("image_preview") val preview: String,
        val url: String,
        val name: String,
        @SerializedName("name_orig") val nameOriginal: String,
        @SerializedName("is_opened") val isOpened: Int,
        @SerializedName("birthday") val birthDay: String?,
        val sex: String
) : Parcelable