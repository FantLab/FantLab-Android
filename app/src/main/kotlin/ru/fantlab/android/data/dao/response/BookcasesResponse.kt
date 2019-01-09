package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.Pageable
import ru.fantlab.android.data.dao.model.Bookcase
import com.google.gson.JsonParser
import ru.fantlab.android.provider.rest.DataManager

data class BookcasesResponse(
        val responses: Pageable<Bookcase>
) {
    class Deserializer(private val perPage: Int) : ResponseDeserializable<BookcasesResponse> {

        override fun deserialize(content: String): BookcasesResponse {
            // TODO replace with actual content when request is ready
            val items: ArrayList<Bookcase> = arrayListOf()
            val bc1 = Bookcase("aaa", "Мои книги", "Книги, имеющиеся в моей библиотеке")
            val bc2 = Bookcase("bbb", "Продаю", "Книги, которые я готов продать или обменять")
            val bc3 = Bookcase("ccc", "Куплю", "Имею желание приобрести эти книги")
            items.add(bc1)
            items.add(bc2)
            items.add(bc3)
            val bookcases = Pageable(0, 3, items)
            return BookcasesResponse(bookcases)
            /*val jsonObject = JsonParser().parse(content).asJsonObject
            val items: ArrayList<Bookcase> = arrayListOf()
            val array = jsonObject.getAsJsonArray("items")
            array.map {
                items.add(DataManager.gson.fromJson(it, Bookcase::class.java))
            }
            val totalCount = jsonObject.getAsJsonPrimitive("total_count").asInt
            val lastPage = (totalCount - 1) / perPage + 1
            val bookcases = Pageable(lastPage, totalCount, items)
            return BookcasesResponse(bookcases)*/
        }
    }
}