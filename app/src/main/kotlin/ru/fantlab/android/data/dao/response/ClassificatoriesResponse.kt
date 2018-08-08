package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.ClassificatorModel
import ru.fantlab.android.provider.rest.DataManager

data class ClassificatoriesResponse(
		val items: ArrayList<ClassificatorModel>
) {
	class Deserializer : ResponseDeserializable<ClassificatoriesResponse> {

		override fun deserialize(content: String): ClassificatoriesResponse {
			val items: ArrayList<ClassificatorModel> = arrayListOf()
			val array = JsonParser().parse(content).asJsonArray
			array.map {
				items.add(DataManager.gson.fromJson(it, ClassificatorModel::class.java))
			}
			return ClassificatoriesResponse(items)
		}
	}
}