package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class Biography(
		val biography: String,
		@SerializedName("biography_notes") val notes: String,
		val compiler: String,
		val source: String,
		@SerializedName("source_link") val sourceLink: String,
		val sites: ArrayList<Site>?,
		val curator: Int?
) {
	data class Site(
			@SerializedName("descr") val description: String,
			val site: String
	)
}