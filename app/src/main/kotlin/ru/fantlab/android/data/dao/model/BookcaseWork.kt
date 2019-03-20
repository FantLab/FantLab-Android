package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class BookcaseWork (
        @SerializedName("autor1_name") val autor1: String?,
        @SerializedName("autor2_name") val autor2: String?,
        @SerializedName("autor_name") val autors: String?,
        @SerializedName("item_id") val itemId: Int,
        @SerializedName("bookcase_item_id") val bookcaseItemId: Int,
        val name: String,
        val rusname: String,
        @SerializedName("item_comment") val comment: String?
) : Parcelable