package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonNull
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.Translator
import ru.fantlab.android.provider.rest.DataManager

data class TranslatorResponse(
        val translator: Translator,
        val sortedWorks: ArrayList<String>,
        val translatedWorks: HashMap<String, Translator.TranslatedWork>
) {
    class Deserializer : ResponseDeserializable<TranslatorResponse> {
        private lateinit var translator: Translator
        private val sortedWorks: ArrayList<String> = arrayListOf()
        private val translatedWorks: HashMap<String, Translator.TranslatedWork> = HashMap()

        override fun deserialize(content: String): TranslatorResponse {
            val jsonObject = JsonParser().parse(content).asJsonObject
            translator = DataManager.gson.fromJson(jsonObject, Translator::class.java)

            if (jsonObject["sorted_works"] != JsonNull.INSTANCE) {
                val array = jsonObject.getAsJsonArray("sorted_works")
                array.map {
                    sortedWorks.add(it.asJsonPrimitive.asString)
                }
            }

            if (jsonObject["translated"] != JsonNull.INSTANCE) {
                val translatedObject = jsonObject.getAsJsonObject("translated")
                val keysArray = translatedObject.keySet()
                keysArray.map {
                    val translatedWork: Translator.TranslatedWork = DataManager.gson.fromJson(translatedObject[it], Translator.TranslatedWork::class.java)
                    translatedWorks.put(it, translatedWork)
                }
            }

            return TranslatorResponse(translator, sortedWorks, translatedWorks)
        }
    }
}