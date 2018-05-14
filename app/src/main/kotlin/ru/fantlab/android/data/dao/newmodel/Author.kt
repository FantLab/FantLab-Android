package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class Author(
		val id: Int,
		val anons: String,
		@SerializedName("birthday") val birthDay: String?,
		@SerializedName("country_id") val countryId: Int?,
		@SerializedName("country_name") val countryName: String,
		@SerializedName("deathday") val deathDay: String?,
		val image: String,
		@SerializedName("image_preview") val preview: String,
		@SerializedName("is_opened") val isOpened: Int,
		val name: String,
		@SerializedName("name_orig") val nameOriginal: String,
		@SerializedName("name_pseudonyms") val namePseudonyms: ArrayList<String>,
		@SerializedName("name_rp") val nameRp: String,
		@SerializedName("name_short") val nameShort: String,
		val sex: String,
		@SerializedName("stat") val statistics: Statistics,
		val url: String
)