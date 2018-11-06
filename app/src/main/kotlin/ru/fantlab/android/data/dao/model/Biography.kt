package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Biography(
		val biography: String,
		@SerializedName("biography_notes") val notes: String,
		val compiler: String,
		val source: String,
		@SerializedName("source_link") val sourceLink: String,
		val sites: ArrayList<Site>?,
		val curator: Int?
) : Parcelable {
	@Keep
	@Parcelize
	data class Site(
			@SerializedName("descr") val description: String,
			val site: String
	) : Parcelable
}