package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Statistics(
		@SerializedName("awardcount") val awardCount: Int?,
		@SerializedName("editioncount") val editionCount: Int?,
		@SerializedName("markcount") val markCount: Int?,
		@SerializedName("moviecount") val movieCount: Int?,
		@SerializedName("responsecount") val responseCount: Int?,
		@SerializedName("workcount") val workCount: Int?
) : Parcelable