package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class BookcaseFilm (
        val country: String,
        val director: String,
        val year: Int,
        @SerializedName("film_id") val filmId: Int,
        @SerializedName("bookcase_item_id") val bookcaseItemId: Int,
        val name: String,
        val rusname: String,
        @SerializedName("item_comment") val comment: String?
) : Parcelable