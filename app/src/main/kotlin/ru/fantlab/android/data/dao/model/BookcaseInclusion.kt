package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class BookcaseInclusion (
    @SerializedName("bookcase_id") val bookcaseId: Int,
    @SerializedName("bookcase_name") val bookcaseName: String,
    @SerializedName("item_added") val itemAdded: Int,
    val bookcaseType: String
) : Parcelable