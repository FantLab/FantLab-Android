package ru.fantlab.android.data.dao.response

data class BookcaseItemCommentUpdateResponse(
        val bookcaseId: Int
) {
    class Parser {

        fun parse(content: String): BookcaseItemCommentUpdateResponse? {
            return BookcaseItemCommentUpdateResponse(0)
        }
    }
}