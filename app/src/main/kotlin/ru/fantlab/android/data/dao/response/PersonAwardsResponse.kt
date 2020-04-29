package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.Nomination
import ru.fantlab.android.provider.rest.DataManager

data class PersonAwardsResponse(
        val awards: ArrayList<Nomination>
) {
    class Deserializer : ResponseDeserializable<PersonAwardsResponse> {

        private val awards: ArrayList<Nomination> = arrayListOf()

        override fun deserialize(content: String): PersonAwardsResponse {
            val array = JsonParser().parse(content).asJsonArray
            array.map {
                awards.add(DataManager.gson.fromJson(it, Nomination::class.java))
            }

            return PersonAwardsResponse(awards)
        }
    }
}