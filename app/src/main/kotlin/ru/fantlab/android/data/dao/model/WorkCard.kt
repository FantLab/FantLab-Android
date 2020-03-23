package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class WorkCard(
        val type: String,
        val id: Int,
        val name: String,
        @SerializedName("name_orig") val nameOriginal: String,
        val title: String,
        val description: String,
        val image: String,
        @SerializedName("image_preview") val preview: String,
        val url: String,
        val year: Int?,
        val published: Int,
        @SerializedName("name_type") val nameType: String,
        @SerializedName("name_type_id") val nameTypeId: Int,
        @SerializedName("name_type_icon") val nameTypeIcon: String,
        @SerializedName("name_type_image") val nameTypeImage: String,
        val statistics: CardStatistics,
        val saga: CardSaga,
        val creators: Edition.Creators
) : Parcelable {
    @Keep
    @Parcelize
    data class CardStatistics(
            val rating: String,
            val voters: Int,
            val responses: Int
    ) : Parcelable

    @Keep
    @Parcelize
    data class CardSaga(
            val type: String,
            val id: Int,
            val name: String,
            val name_type: String
    ) : Parcelable
}