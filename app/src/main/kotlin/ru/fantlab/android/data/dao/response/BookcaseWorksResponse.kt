package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.BookcaseWork
import ru.fantlab.android.provider.rest.DataManager

data class BookcaseWorksResponse(
        val works: ArrayList<BookcaseWork>
) {
    class Deserializer(private val perPage: Int) : ResponseDeserializable<BookcaseWorksResponse> {

        override fun deserialize(content: String): BookcaseWorksResponse {
            val jsonObject = JsonParser().parse(content).asJsonObject
            val items: ArrayList<BookcaseWork> = arrayListOf()
            val array = jsonObject.getAsJsonArray("bookcase_items")
            array.map {
                items.add(DataManager.gson.fromJson(it, BookcaseWork::class.java))
            }
            return BookcaseWorksResponse(items)
        }
    }
}