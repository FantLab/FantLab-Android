package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class AdditionalImages(
		val cover: List<Cover>,
		val plus: List<Image>?
)

data class Cover(
		val image: String,
		@SerializedName("image_preview") val preview: String?,
		@SerializedName("image_spine") val spine: String?,
		@SerializedName("pic_text") val text: String
)

data class Image(
		val image: String,
		@SerializedName("image_preview") val preview: String?,
		@SerializedName("pic_text") val text: String
)