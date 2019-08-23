package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Bookcase (
        @SerializedName("bookcase_comment") val bookcaseComment: String?,
        @SerializedName("bookcase_group") val bookcaseGroup: String,
        @SerializedName("bookcase_id") val bookcaseId: Int,
        @SerializedName("bookcase_name") val bookcaseName: String,
        @SerializedName("bookcase_shared") val bookcaseShared: Int,
        @SerializedName("bookcase_type") val bookcaseType: String,
        @SerializedName("date_of_add") val bookcaseAdded: String,
        @SerializedName("date_of_edit") val bookcaseModified: String?,
        @SerializedName("item_count") val itemCount: Int,
        val sort: Int
) : Parcelable
