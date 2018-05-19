package ru.fantlab.android.data.dao.newmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Translation(
		@SerializedName("lang") val language: String,
		@SerializedName("lang_code") val languageCode: String,
		@SerializedName("lang_id") val languageId: Int,
		val translations: ArrayList<WorkTranslation>
) : Parcelable {
	@Parcelize
	data class WorkTranslation(
			val titles: ArrayList<Title>,
			val translators: ArrayList<Translator>,
			val year: Int
	) : Parcelable

	@Parcelize
	data class Title(
			@SerializedName("count") val editionCount: Int,
			val title: String
	) : Parcelable

	@Parcelize
	data class Translator(
			val id: Int,
			val name: String,
			@SerializedName("short_name") val shortName: String,
			val type: String
	) : Parcelable
}