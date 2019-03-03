package ru.fantlab.android.data.dao.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.JsonParser
import ru.fantlab.android.data.dao.model.Award
import ru.fantlab.android.provider.rest.DataManager

data class ContestResponse(
		val contest: List<Pair<String, List<Award.ContestWork>>>?
) {
	class Deserializer : ResponseDeserializable<ContestResponse> {
		private lateinit var contest: Award.Contest

		override fun deserialize(content: String): ContestResponse {
			val jsonObject = JsonParser().parse(content).asJsonObject

			contest = DataManager.gson.fromJson(jsonObject, Award.Contest::class.java)

			val result = contest.contestWorks?.groupBy { it.nominationRusname }?.entries?.map { (name, group) ->
				Pair(name, group)
			}

			return ContestResponse(result)
		}
	}
}