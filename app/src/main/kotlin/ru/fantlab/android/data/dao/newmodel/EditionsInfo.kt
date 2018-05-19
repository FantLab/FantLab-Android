package ru.fantlab.android.data.dao.newmodel

import android.os.Parcelable
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import ru.fantlab.android.provider.rest.DataManager
import java.util.*

@Parcelize
data class EditionsInfo(
		val allCount: Int,
		val bookTypes: ArrayList<BookType>,
		val isbns: ArrayList<String>,
		val languages: ArrayList<Language>,
		val translators: ArrayList<Translator>
) : Parcelable {
	@Parcelize
	data class BookType(
			@SerializedName("count") val editionCount: Int,
			val id: Int,
			val name: String,
			val title: String
	) : Parcelable

	@Parcelize
	data class Language(
			@SerializedName("count") val editionCount: Int,
			@SerializedName("lang_code") val code: String,
			@SerializedName("lang_id") val id: Int,
			@SerializedName("lang_name") val name: String
	) : Parcelable

	@Parcelize
	data class Translator(
			@SerializedName("count") val editionCount: Int,
			val id: Int,
			val name: String
	) : Parcelable

	class Deserializer : ResponseDeserializable<EditionsInfo> {

		private var allCount: Int = 0
		private val bookTypes: ArrayList<BookType> = arrayListOf()
		private val isbns: ArrayList<String> = arrayListOf()
		private val languages: ArrayList<Language> = arrayListOf()
		private val translators: ArrayList<Translator> = arrayListOf()

		override fun deserialize(content: String): EditionsInfo {
			val jsonObject = JsonParser().parse(content).asJsonObject
			allCount = jsonObject.getAsJsonPrimitive("all").asInt
			var `object` = jsonObject.getAsJsonObject("booktype")
			`object`.entrySet().map {
				val bookTypeObject = it.value.asJsonObject
				val bookType = DataManager.gson.fromJson(bookTypeObject.toString(), BookType::class.java)
				bookTypes.add(bookType)
			}
			var array = jsonObject.getAsJsonArray("isbn_list")
			array?.map {
				isbns.add(it.toString())
			}
			`object` = jsonObject.getAsJsonObject("langs")
			`object`.entrySet().map {
				val languageObject = it.value.asJsonObject
				val language = DataManager.gson.fromJson(languageObject.toString(), Language::class.java)
				languages.add(language)
			}
			array = jsonObject.getAsJsonArray("translators")
			array.map {
				val translatorObject = it.asJsonObject
				val translator = DataManager.gson.fromJson(translatorObject.toString(), Translator::class.java)
				translators.add(translator)
			}
			return EditionsInfo(
					allCount,
					bookTypes,
					isbns,
					languages,
					translators
			)
		}
	}
}