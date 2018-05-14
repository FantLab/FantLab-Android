package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class Statistics(
		@SerializedName("awardcount") val awardCount: Int?,
		@SerializedName("editioncount") val editionCount: Int?,
		@SerializedName("markcount") val markCount: Int?,
		@SerializedName("moviecount") val movieCount: Int?,
		@SerializedName("responsecount") val responseCount: Int?,
		@SerializedName("workcount") val workCount: Int?
)