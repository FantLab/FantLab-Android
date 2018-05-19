package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonNull
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.newmodel.*
import ru.fantlab.android.provider.rest.DataManager

data class AuthorResponse(
		val author: Author,
		val biography: Biography?,
		val blogAnons: BlogAnons?,
		val registeredUser: AuthorUser?,
		val awards: Awards?,
		val linguaProfile: ArrayList<String>,
		val cycles: WorksBlocks?,
		val works: WorksBlocks?
) {
	class Deserializer : ResponseDeserializable<AuthorResponse> {

		private lateinit var author: Author
		private var biography: Biography? = null
		private var blogAnons: BlogAnons? = null
		private var registeredUser: AuthorUser? = null
		private var awards: Awards? = null
		private val linguaProfile: ArrayList<String> = arrayListOf()
		private var cycles: WorksBlocks? = null
		private var works: WorksBlocks? = null

		override fun deserialize(content: String): AuthorResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject
			author = DataManager.gson.fromJson(jsonObject, Author::class.java)
			if (jsonObject["biography"] != JsonNull.INSTANCE) {
				biography = DataManager.gson.fromJson(jsonObject, Biography::class.java)
			}
			if (jsonObject["fl_blog_anons"] != JsonNull.INSTANCE) {
				val `object` = jsonObject.getAsJsonObject("fl_blog_anons")
				blogAnons = DataManager.gson.fromJson(`object`, BlogAnons::class.java)
			}
			if (jsonObject["registered_user_id"] != JsonNull.INSTANCE) {
				registeredUser = DataManager.gson.fromJson(jsonObject, AuthorUser::class.java)
			}
			if (jsonObject["awards"] != JsonNull.INSTANCE) {
				val `object` = jsonObject.getAsJsonObject("awards")
				awards = DataManager.gson.fromJson(`object`, Awards::class.java)
			}
			if (jsonObject["la_resume"] != JsonNull.INSTANCE) {
				val array = jsonObject.getAsJsonArray("la_resume")
				array.map {
					linguaProfile.add(it.asJsonPrimitive.asString)
				}
			}
			if (jsonObject["cycles_blocks"] != JsonNull.INSTANCE) {
				val `object` = jsonObject.getAsJsonObject("cycles_blocks")
				cycles = WorksBlocks.Deserializer().deserialize(`object`.toString())
			}
			if (jsonObject["works_blocks"] != JsonNull.INSTANCE) {
				val `object` = jsonObject.getAsJsonObject("works_blocks")
				works = WorksBlocks.Deserializer().deserialize(`object`.toString())
			}
			return AuthorResponse(
					author,
					biography,
					blogAnons,
					registeredUser,
					awards,
					linguaProfile,
					cycles,
					works
			)
		}
	}
}