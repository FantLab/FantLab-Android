package ru.fantlab.android.data.dao.newmodel

import com.google.gson.annotations.SerializedName

data class EditionsBlocks(
		// todo посмотреть в базе, выписать остальные типы
		@SerializedName("1") val booksPlan: EditionsBlock,
		@SerializedName("10") val paper: EditionsBlock,
		@SerializedName("30") val audio: EditionsBlock,
		@SerializedName("34") val digital: EditionsBlock,
		@SerializedName("80") val foreign: EditionsBlock
) {
	data class EditionsBlock(
			val block: String,
			val id: Int,
			val list: List<Edition>,
			val name: String,
			val title: String
	)

	data class Edition(
			@SerializedName("autors") val authors: String,
			val compilers: String,
			@SerializedName("correct_level") val correctLevel: Float,
			val ebook: Int,
			@SerializedName("edition_id") val editionId: Int,
			val isbn: String,
			@SerializedName("lang") val language: String,
			@SerializedName("lang_code") val languageCode: String,
			@SerializedName("lang_id") val languageId: Int,
			val name: String,
			@SerializedName("plandate") val planDate: Long?,
			@SerializedName("plandate_txt") val planYear: String,
			val type: Int,
			val year: Int
	)
}