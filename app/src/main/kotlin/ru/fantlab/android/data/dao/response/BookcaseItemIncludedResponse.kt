package ru.fantlab.android.data.dao.response

data class BookcaseItemIncludedResponse(
        val bookcaseId: Int
) {
    class Parser {

        fun parse(content: String): BookcaseItemIncludedResponse? {
            return BookcaseItemIncludedResponse(0)
        }
    }
}