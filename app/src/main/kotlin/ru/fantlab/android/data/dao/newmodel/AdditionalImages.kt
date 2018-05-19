package ru.fantlab.android.data.dao.newmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdditionalImages(
		val cover: List<Cover>,
		val plus: List<Image>?
) : Parcelable {
	@Parcelize
	data class Cover(
			val image: String,
			@SerializedName("image_preview") val preview: String?,
			@SerializedName("image_spine") val spine: String?,
			@SerializedName("pic_text") val text: String
	) : Parcelable

	@Parcelize
	data class Image(
			val image: String,
			@SerializedName("image_preview") val preview: String?,
			@SerializedName("pic_text") val text: String
	) : Parcelable
}