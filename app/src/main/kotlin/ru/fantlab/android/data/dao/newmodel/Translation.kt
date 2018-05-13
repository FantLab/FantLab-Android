package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class Translation(
		@SerializedName("lang") val language: String,
		@SerializedName("lang_code") val languageCode: String,
		@SerializedName("lang_id") val languageId: Int,
		val translations: ArrayList<WorkTranslation>
) {
	data class WorkTranslation(
			val titles: ArrayList<Title>,
			val translators: ArrayList<Translator>,
			val year: Int
	)

	data class Title(
			@SerializedName("count") val editionCount: Int,
			val title: String
	)

	data class Translator(
			val id: Int,
			val name: String,
			@SerializedName("short_name") val shortName: String,
			val type: String
	)
}