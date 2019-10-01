package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.Translator
import ru.fantlab.android.provider.rest.DataManager

data class TranslatorResponse(
        val translator: Translator
) {
    class Deserializer : ResponseDeserializable<TranslatorResponse> {
        private lateinit var translator: Translator

        override fun deserialize(content: String): TranslatorResponse {
            val jsonObject = JsonParser().parse(content).asJsonObject
            translator = DataManager.gson.fromJson(jsonObject, Translator::class.java)
            return TranslatorResponse(translator)
        }
    }
}