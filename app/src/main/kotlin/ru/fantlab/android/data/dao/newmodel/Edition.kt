package ru.fantlab.android.data.dao.newmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Edition(
		@SerializedName("copies") val copyCount: Int,
		@SerializedName("correct_level") val correctLevel: Float,
		@SerializedName("cover_type") val coverType: String,
		val creators: Creators,
		val description: String,
		@SerializedName("edition_id") val id: Int,
		@SerializedName("edition_name") val name: String,
		@SerializedName("edition_type") val type: String,
		@SerializedName("edition_type_plus") val additionalTypes: ArrayList<String>,
		val format: String,
		@SerializedName("format_mm") val formatMm: String?,
		val image: String,
		@SerializedName("image_preview") val preview: String,
		val isbns: ArrayList<String>,
		@SerializedName("lang") val language: String,
		@SerializedName("lang_code") val languageCode: String,
		val notes: String,
		val pages: Int,
		@SerializedName("plan_date") val planDate: String,
		@SerializedName("plan_description") val planDescription: String,
		@SerializedName("preread") val preRead: Int,
		val series: ArrayList<Series>,
		@SerializedName("type") val typeId: Int,
		val year: Int
) : Parcelable {
	@Parcelize
	data class Creators(
			val authors: ArrayList<Author>?,
			val compilers: ArrayList<Compiler>?,
			val publishers: ArrayList<Publisher>?
	) : Parcelable

	@Parcelize
	data class Author(
			val id: Int,
			@SerializedName("is_opened") val isOpened: Int,
			val name: String,
			val type: String
	) : Parcelable

	@Parcelize
	data class Compiler(
			val id: Int?,
			val name: String,
			val type: String?
	) : Parcelable

	@Parcelize
	data class Publisher(
			val id: Int?,
			val name: String,
			val type: String?
	) : Parcelable

	@Parcelize
	data class Series(
			val id: Int,
			@SerializedName("is_opened") val isOpened: Int,
			val name: String,
			val type: String
	) : Parcelable
}