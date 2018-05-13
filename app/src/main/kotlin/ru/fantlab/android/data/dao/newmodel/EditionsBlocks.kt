package ru.fantlab.android.data.dao.newmodel

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import ru.fantlab.android.provider.rest.DataManager

data class EditionsBlocks(
		val editionsBlocks: ArrayList<EditionsBlock>
) {
	data class EditionsBlock(
			val block: String,
			val id: Int,
			val list: ArrayList<Edition>,
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
			val translators: String?,
			val type: Int,
			val year: Int
	)

	class Deserializer : ResponseDeserializable<EditionsBlocks> {

		private val editionsBlocks: ArrayList<EditionsBlock> = arrayListOf()

		override fun deserialize(content: String): EditionsBlocks {
			val jsonObject = JsonParser().parse(content).asJsonObject
			jsonObject.entrySet().map {
				val blockObject = it.value.asJsonObject
				val block = DataManager.gson.fromJson(blockObject.toString(), EditionsBlock::class.java)
				editionsBlocks.add(block)
			}
			return EditionsBlocks(editionsBlocks)
		}
	}
}