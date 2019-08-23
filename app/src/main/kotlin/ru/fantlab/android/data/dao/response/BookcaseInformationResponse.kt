package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.Bookcase
import ru.fantlab.android.provider.rest.DataManager

data class BookcaseInformationResponse(
        val bookcase: Bookcase
) {
    class Deserializer : ResponseDeserializable<BookcaseInformationResponse> {
        override fun deserialize(content: String): BookcaseInformationResponse {
            val jsonObject = JsonParser().parse(content).asJsonObject
            return BookcaseInformationResponse(DataManager.gson.fromJson(jsonObject, Bookcase::class.java))
        }
    }
}