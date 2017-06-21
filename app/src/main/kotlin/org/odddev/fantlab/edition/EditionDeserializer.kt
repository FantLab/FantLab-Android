package org.odddev.fantlab.edition

import com.google.gson.*
import org.odddev.fantlab.core.utils.getField
import java.lang.reflect.Type

class EditionDeserializer : JsonDeserializer<Edition> {

	fun JsonArray.parseCreators(creators: ArrayList<Edition.Creator>) {
		this.map { it.asJsonObject }.mapTo(creators) { Edition.Creator(
				id = it.get("id").asInt,
				isOpened = it.get("is_opened").getField()?.asInt == 1,
				name = it.get("name").asString,
				type = it.get("type").asString
		) }
	}

	fun JsonObject.parsePicture(): Edition.Picture = Edition.Picture(
			check = this.get("check").asInt == 1,
			image = this.get("image").asString,
			preview = this.get("image_preview").asString,
			copyright = this.get("pic_copyright").getField()?.asString ?: "",
			orig = this.get("pic_orig").asInt == 1,
			text = this.get("pic_text").asString,
			type = this.get("pic_type").asInt
	)

	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Edition {
		val jsonObject = json.asJsonObject

		val content = ArrayList<String>()
		jsonObject.getAsJsonArray("content").mapTo(content) { it.asString }

		val creators = HashMap<String, List<Edition.Creator>>()
		for ((key, value) in jsonObject.getAsJsonObject("creators").entrySet()) {
			val someCreators = ArrayList<Edition.Creator>()
			value ?: value.asJsonArray.parseCreators(someCreators)
			creators.put(key, someCreators)
		}

		val types = ArrayList<String>()
		types.add(jsonObject.get("editiontype").asString)
		jsonObject.getAsJsonArray("editiontype_plus").mapTo(types) { it.asString }

		val format = "${jsonObject.get("format").asString} ${jsonObject.get("format_mm").asString}"

		val imagesObject = jsonObject.getAsJsonObject("images_plus")
		val images = ArrayList<Edition.Picture>()
		for ((_, value) in imagesObject?.getAsJsonObject("cover")?.entrySet() ?: emptySet()) {
			images.add(value.asJsonObject.parsePicture())
		}
		imagesObject?.getAsJsonArray("plus")?.mapTo(images) { it.asJsonObject.parsePicture() }

		val isbns = ArrayList<String>()
		jsonObject.getAsJsonArray("isbns").mapTo(isbns) { it.asString }

		val language = Edition.Language(
				code = jsonObject.get("lang_code").asString,
				name = jsonObject.get("lang").asString
		)

		val plan = Edition.Plan(
				date = jsonObject.get("plan_date").asString,
				description = jsonObject.get("plan_description").asString
		)

		return Edition(
				content = content,
				copies = jsonObject.get("copies").getField()?.asInt ?: -1,
				correctLevel = jsonObject.get("correct_level").asFloat,
				covertType = jsonObject.get("covertype").getField()?.asString ?: "",
				creators = creators,
				description = jsonObject.get("description").getField()?.asString ?: "",
				id = jsonObject.get("edition_id").asInt,
				types = types,
				format = format,
				images = images,
				isbns = isbns,
				language = language,
				notes = jsonObject.get("notes").asString,
				origName = jsonObject.get("origname").asString,
				pages = jsonObject.get("pages").getField()?.asInt ?: -1,
				plan = plan,
				series = jsonObject.get("series").getField()?.asString ?: "",
				type = jsonObject.get("type").asInt,
				year = jsonObject.get("year").getField()?.asInt ?: -1
		)
	}
}