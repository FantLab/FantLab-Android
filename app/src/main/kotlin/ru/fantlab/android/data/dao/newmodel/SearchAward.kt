package ru.fantlab.android.data.dao.newmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchAward(
		@SerializedName("award_id") val id: Int,
		val country: String,
		@SerializedName("country_id") val countryId: Int,
		val description: String,
		@SerializedName("lang_id") val languageId: Int,
		val name: String,
		val notes: String,
		@SerializedName("rusname") val rusName: String,
		val type: Int,
		@SerializedName("year_close") val closeYear: Int,
		@SerializedName("year_open") val openYear: Int
) : Parcelable