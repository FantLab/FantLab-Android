package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonNull
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.provider.rest.DataManager

data class AuthorResponse(
		val author: Author,
		val biography: Biography?,
		val blogAnons: BlogAnons?,
		val classificatory: ArrayList<GenreGroup>,
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
		private val classificatory: ArrayList<GenreGroup> = arrayListOf()
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
			if (jsonObject["classificatory"] != JsonNull.INSTANCE) {
				val array = jsonObject.getAsJsonObject("classificatory").getAsJsonArray("genre_group")
				if (array != null) {
					val recursiveClassificatory = arrayListOf<RecursiveGenreGroup>()
					array.map {
						recursiveClassificatory.add(DataManager.gson.fromJson(
								it.asJsonObject,
								RecursiveGenreGroup::class.java
						))
					}
					recursiveClassificatory.map {
						val genres = arrayListOf<Pair<Int, GenreGroup.Genre>>()
						it.genre.map { genre ->
							genres.add(0, genre)
						}
						classificatory.add(GenreGroup(genres, it.genreGroupId, it.label))
					}
				}
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
					classificatory,
					registeredUser,
					awards,
					linguaProfile,
					cycles,
					works
			)
		}

		private fun ArrayList<Pair<Int, GenreGroup.Genre>>.add(
				level: Int,
				genreGroup: RecursiveGenreGroup.Genre
		) {
			add(level to GenreGroup.Genre(genreGroup.genreId, genreGroup.label, genreGroup.percent))
			genreGroup.genre?.map { add(level + 1, it) }
		}
	}
}