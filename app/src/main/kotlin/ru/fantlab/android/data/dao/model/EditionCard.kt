package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class EditionCard(
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
        val year: Int,
        val published: Int,
        @SerializedName("lang") val language: String,
        @SerializedName("lang_code") val languageCode: String,
        @SerializedName("copies") val copyCount: Int,
        val isbns: ArrayList<String>,
        val creators: Edition.Creators,
        val series: ArrayList<Edition.Series>
) : Parcelable