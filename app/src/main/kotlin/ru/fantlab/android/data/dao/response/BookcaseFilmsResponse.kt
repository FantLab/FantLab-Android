package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.BookcaseFilm
import ru.fantlab.android.provider.rest.DataManager

data class BookcaseFilmsResponse(
        val films: Pageable<BookcaseFilm>
) {
    class Deserializer(private val perPage: Int) : ResponseDeserializable<BookcaseFilmsResponse> {

        override fun deserialize(content: String): BookcaseFilmsResponse {
            val jsonObject = JsonParser().parse(content).asJsonObject
            val items: ArrayList<BookcaseFilm> = arrayListOf()
            val array = jsonObject.getAsJsonArray("bookcase_items")
            array.map {
                items.add(DataManager.gson.fromJson(it, BookcaseFilm::class.java))
            }
            val totalCount = jsonObject.getAsJsonPrimitive("count").asInt
            val lastPage = (totalCount - 1) / perPage + 1
            val films = Pageable(lastPage, totalCount, items)
            return BookcaseFilmsResponse(films)
        }
    }
}