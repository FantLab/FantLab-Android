package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.BookcaseInclusion
import ru.fantlab.android.provider.rest.DataManager

data class BookcaseInclusionResponse(
        val items: ArrayList<BookcaseInclusion>
) {
    class Deserializer : ResponseDeserializable<BookcaseInclusionResponse> {

        private val bookcases: ArrayList<BookcaseInclusion> = arrayListOf()

        override fun deserialize(content: String): BookcaseInclusionResponse {
            val array = JsonParser().parse(content).asJsonArray
            array.map {
                bookcases.add(DataManager.gson.fromJson(it, BookcaseInclusion::class.java))
            }
            return BookcaseInclusionResponse(bookcases)
        }
    }
}