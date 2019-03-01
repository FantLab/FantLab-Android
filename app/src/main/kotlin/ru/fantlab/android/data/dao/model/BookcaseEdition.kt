package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class BookcaseEdition (
        val name: String,
        @SerializedName("bookcase_item_id") val itemId: Int,
        val autors: String,
        @SerializedName("edition_id") val editionId: Int,
        val publisher: String,
        val year: Int,
        @SerializedName("item_comment") val comment: String
) : Parcelable