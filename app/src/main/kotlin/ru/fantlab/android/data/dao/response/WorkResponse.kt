package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonNull
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.provider.rest.DataManager

data class WorkResponse(
		val work: Work,
		val awards: Awards?,
		val children: ArrayList<ChildWork>,
		val classificatory: ArrayList<GenreGroup>,
		val editionsBlocks: EditionsBlocks?,
		val editionsInfo: EditionsInfo?,
		val films: Films?,
		val linguaProfile: ArrayList<String>,
		val parents: ParentWorks?,
		val rootSagas: ArrayList<WorkRootSaga>,
		val statistics: Statistics?,
		val translations: ArrayList<Translation>
) {
	class Deserializer : ResponseDeserializable<WorkResponse> {

		private lateinit var work: Work
		private var awards: Awards? = null
		private val children: ArrayList<ChildWork> = arrayListOf()
		private val classificatory: ArrayList<GenreGroup> = arrayListOf()
		private var editions: EditionsBlocks? = null
		private var editionsInfo: EditionsInfo? = null
		private var films: Films? = null
		private val linguaProfile: ArrayList<String> = arrayListOf()
		private var parents: ParentWorks? = null
		private val rootSagas: ArrayList<WorkRootSaga> = arrayListOf()
		private var statistics: Statistics? = null
		private val translations: ArrayList<Translation> = arrayListOf()

		override fun deserialize(content: String): WorkResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			work = DataManager.gson.fromJson(jsonObject, Work::class.java)
			if (jsonObject["awards"] != JsonNull.INSTANCE) {
				val `object` = jsonObject.getAsJsonObject("awards")
				awards = DataManager.gson.fromJson(`object`, Awards::class.java)
			}
			if (jsonObject["children"] != JsonNull.INSTANCE) {
				val array = jsonObject.getAsJsonArray("children")
				array.map {
					children.add(DataManager.gson.fromJson(it.asJsonObject, ChildWork::class.java))
				}
			}
			if (jsonObject["classificatory"] != JsonNull.INSTANCE) {
				val array = jsonObject.getAsJsonArray("classificatory")
				array.map {
					classificatory.add(DataManager.gson.fromJson(it.asJsonObject, GenreGroup::class.java))
				}
			}
			if (jsonObject["editions_blocks"] != JsonNull.INSTANCE) {
				val `object` = jsonObject.getAsJsonObject("editions_blocks")
				editions = EditionsBlocks.Deserializer().deserialize(`object`.toString())
			}
			if (jsonObject["editions_info"] != JsonNull.INSTANCE) {
				val `object` = jsonObject.getAsJsonObject("editions_info")
				editionsInfo = EditionsInfo.Deserializer().deserialize(`object`.toString())
			}
			if (jsonObject["films"] != JsonNull.INSTANCE) {
				val `object` = jsonObject.getAsJsonObject("films")
				films = DataManager.gson.fromJson(`object`, Films::class.java)
			}
			if (jsonObject["la_resume"] != JsonNull.INSTANCE) {
				val array = jsonObject.getAsJsonArray("la_resume")
				array.map {
					linguaProfile.add(it.asJsonPrimitive.asString)
				}
			}
			if (jsonObject["parents"] != JsonNull.INSTANCE) {
				val `object` = jsonObject.getAsJsonObject("parents")
				parents = DataManager.gson.fromJson(`object`, ParentWorks::class.java)
			}
			if (jsonObject["work_root_saga"] != JsonNull.INSTANCE) {
				val array = jsonObject.getAsJsonArray("work_root_saga")
				array.map {
					rootSagas.add(DataManager.gson.fromJson(it.asJsonObject, WorkRootSaga::class.java))
				}
			}
			if (jsonObject["stat"] != JsonNull.INSTANCE) {
				val `object` = jsonObject.getAsJsonObject("stat")
				statistics = DataManager.gson.fromJson(`object`, Statistics::class.java)
			}
			if (jsonObject["translations"] != JsonNull.INSTANCE) {
				val array = jsonObject.getAsJsonArray("translations")
				array.map {
					translations.add(DataManager.gson.fromJson(it.asJsonObject, Translation::class.java))
				}
			}
			return WorkResponse(
					work,
					awards,
					children,
					classificatory,
					editions,
					editionsInfo,
					films,
					linguaProfile,
					parents,
					rootSagas,
					statistics,
					translations
			)
		}
	}
}