package ru.fantlab.android.data.dao.response

data class CreateBookcaseResponse(
        val bookcaseId: Int
) {
    class Parser {

        fun parse(content: String): CreateBookcaseResponse? {
            /*val pattern = Regex("r(\\d+)+=(\\d+)")
            val results = pattern.matchEntire(content)?.groupValues
            if (results != null) {
                val responseId = results[1].toInt()
                val votesCount = results[2].toInt()
                return VoteResponse(responseId, votesCount)
            }*/
            return null
        }
    }
}