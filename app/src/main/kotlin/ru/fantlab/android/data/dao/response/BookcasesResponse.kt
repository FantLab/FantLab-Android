package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import ru.fantlab.android.data.dao.model.Bookcase

data class BookcasesResponse(
        val items: ArrayList<Bookcase>
) {
    class Deserializer : ResponseDeserializable<BookcasesResponse> {

        override fun deserialize(content: String): BookcasesResponse {
            // TODO replace with actual content when request is ready
            val items: ArrayList<Bookcase> = arrayListOf()
            val bc1 = Bookcase("aaa", "Мои книги", "Книги, имеющиеся в моей библиотеке", "edition")
            val bc2 = Bookcase("bbb", "Продаю", "Книги, которые я готов продать или обменять", "edition")
            val bc3 = Bookcase("ccc", "Куплю", "Имею желание приобрести эти книги", "edition")
            items.add(bc1)
            items.add(bc2)
            items.add(bc3)
            return BookcasesResponse(items)
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