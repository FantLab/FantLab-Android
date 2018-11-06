package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Translation(
		@SerializedName("lang") val language: String,
		@SerializedName("lang_code") val languageCode: String,
		@SerializedName("lang_id") val languageId: Int,
		val translations: ArrayList<WorkTranslation>
) : Parcelable {
	@Keep
	@Parcelize
	data class WorkTranslation(
			val titles: ArrayList<Title>,
			val translators: ArrayList<Translator>,
			val year: Int
	) : Parcelable

	@Keep
	@Parcelize
	data class Title(
			@SerializedName("count") val editionCount: Int,
			val title: String
	) : Parcelable

	@Keep
	@Parcelize
	data class Translator(
			val id: Int,
			val name: String,
			@SerializedName("short_name") val shortName: String,
			val type: String
	) : Parcelable
}