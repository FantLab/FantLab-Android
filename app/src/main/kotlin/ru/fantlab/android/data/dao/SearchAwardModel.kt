package ru.fantlab.android.data.dao

import com.google.gson.annotations.SerializedName

data class SearchAwardModel(
		var awardId: Int,
		var country: String,
		var countryId: Int,
		var description: String,
		var langId: Int,
		var name: String,
		var notes: String,
		@SerializedName("rusname")
		var rusName: String,
		var type: Int,
		var yearClose: Int,
		var yearOpen: Int
)