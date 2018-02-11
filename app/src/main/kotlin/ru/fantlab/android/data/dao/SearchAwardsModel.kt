package ru.fantlab.android.data.dao

import com.google.gson.annotations.SerializedName

data class SearchAwardsModel(
		val awardId: Int,
		val country: String,
		val countryId: Int,
		val description: String,
		val langId: Int,
		val name: String,
		val notes: String,
		@SerializedName("rusname")
		val rusName: String,
		val type: Int,
		val yearClose: Int,
		val yearOpen: Int
)