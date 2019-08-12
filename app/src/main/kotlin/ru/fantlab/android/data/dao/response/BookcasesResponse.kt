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
            /*val items: ArrayList<Bookcase> = arrayListOf()
            val bc1 = Bookcase(3059, "Прочитать", "В очереди на прочтение", "work")
            val bc2 = Bookcase(3056, "Мои книги", "Книги, имеющиеся в моей библиотеке", "edition")
            val bc3 = Bookcase(3057, "Продаю", "Книги, которые я готов продать или обменять", "edition")
            val bc4 = Bookcase(3058, "Куплю", "Имею желание приобрести эти книги", "edition")
            val bc5 = Bookcase(177011, "Просмотренное", "Фантастические фильмы, просмотренные за прошлый год", "film")
            items.add(bc1)
            items.add(bc2)
            items.add(bc3)
            items.add(bc4)
            items.add(bc5)
            return BookcasesResponse(items)*/
            val array = JsonParser().parse(content).asJsonArray
            array.map {
                bookcases.add(DataManager.gson.fromJson(it, Bookcase::class.java))
            }
            return BookcasesResponse(bookcases)
        }
    }
}