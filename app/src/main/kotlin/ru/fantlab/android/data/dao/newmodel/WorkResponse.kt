package ru.fantlab.android.data.dao.newmodel

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
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
		private var editionsBlocks: EditionsBlocks? = null
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
			var `object` = jsonObject.getAsJsonObject("awards")
			awards = DataManager.gson.fromJson(`object`, Awards::class.java)
			var array = jsonObject.getAsJsonArray("children")
			array.map {
				children.add(DataManager.gson.fromJson(it.asJsonObject, ChildWork::class.java))
			}
			array = jsonObject.getAsJsonArray("classificatory")
			array.map {
				classificatory.add(DataManager.gson.fromJson(it.asJsonObject, GenreGroup::class.java))
			}
			`object` = jsonObject.getAsJsonObject("editions_blocks")
			editionsBlocks = EditionsBlocks.Deserializer().deserialize(`object`.toString())
			`object` = jsonObject.getAsJsonObject("editions_info")
			editionsInfo = EditionsInfo.Deserializer().deserialize(`object`.toString())
			`object` = jsonObject.getAsJsonObject("films")
			films = DataManager.gson.fromJson(`object`, Films::class.java)
			array = jsonObject.getAsJsonArray("la_resume")
			array.map {
				linguaProfile.add(it.asJsonPrimitive.asString)
			}
			`object` = jsonObject.getAsJsonObject("parents")
			parents = DataManager.gson.fromJson(`object`, ParentWorks::class.java)
			array = jsonObject.getAsJsonArray("work_root_saga")
			array.map {
				rootSagas.add(DataManager.gson.fromJson(it.asJsonObject, WorkRootSaga::class.java))
			}
			`object` = jsonObject.getAsJsonObject("stat")
			statistics = DataManager.gson.fromJson(`object`, Statistics::class.java)
			array = jsonObject.getAsJsonArray("translations")
			array.map {
				translations.add(DataManager.gson.fromJson(it.asJsonObject, Translation::class.java))
			}
			return WorkResponse(
					work,
					awards,
					children,
					classificatory,
					editionsBlocks,
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