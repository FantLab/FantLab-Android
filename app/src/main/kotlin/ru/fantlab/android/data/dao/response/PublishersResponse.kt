package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.*
import ru.fantlab.android.provider.rest.DataManager

data class PublishersResponse(
		val publishers: Pageable<Publishers.Publisher>
) {
	class Deserializer(private val perPage: Int) : ResponseDeserializable<PublishersResponse> {

		override fun deserialize(content: String): PublishersResponse {
			val items: ArrayList<Publishers.Publisher> = arrayListOf()
			val array = JsonParser().parse(content).asJsonArray
			array.map {
				items.add(DataManager.gson.fromJson(it, Publishers.Publisher::class.java))
			}
			val totalCount = array.size()
			val lastPage = (totalCount - 1) / perPage + 1
			val publishers = Pageable(lastPage, totalCount, items)
			return PublishersResponse(publishers)
		}


	}
}