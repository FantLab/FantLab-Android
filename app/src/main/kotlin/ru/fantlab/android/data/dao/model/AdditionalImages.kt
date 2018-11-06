package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class AdditionalImages(
		val cover: List<Cover>,
		val plus: List<Image>?
) : Parcelable {
	@Keep
	@Parcelize
	data class Cover(
			val image: String,
			@SerializedName("image_preview") val preview: String?,
			@SerializedName("image_spine") val spine: String?,
			@SerializedName("pic_text") val text: String
	) : Parcelable

	@Keep
	@Parcelize
	data class Image(
			val image: String,
			@SerializedName("image_preview") val preview: String?,
			@SerializedName("pic_text") val text: String
	) : Parcelable
}