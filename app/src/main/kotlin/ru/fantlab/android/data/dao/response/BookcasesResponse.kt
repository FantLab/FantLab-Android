package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.provider.rest.DataManager

data class BookcasesResponse(
        val items: ArrayList<Bookcase>
) {
    class Deserializer : ResponseDeserializable<BookcasesResponse> {

        private val bookcases: ArrayList<Bookcase> = arrayListOf()

        override fun deserialize(content: String): BookcasesResponse {
            val array = JsonParser().parse(content).asJsonArray
            array.map {
                bookcases.add(DataManager.gson.fromJson(it, Bookcase::class.java))
            }
            return BookcasesResponse(bookcases)
        }
    }
}