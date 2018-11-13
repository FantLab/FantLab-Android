package ru.fantlab.android.data.dao.response

data class VoteResponse(
		val responseId: String,
		val votesCount: String
) {
	class Parser {

		fun parse(content: String): VoteResponse? {
			val pattern = Regex("r(\\d+)+=(\\d+)")
			val results = pattern.matchEntire(content)?.groupValues
			if (results != null) {
				val responseId = results[1]
				val votesCount = results[2]
				return VoteResponse(responseId, votesCount)
			}
			return null
		}
	}
}