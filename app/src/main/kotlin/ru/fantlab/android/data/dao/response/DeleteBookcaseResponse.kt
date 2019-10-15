package ru.fantlab.android.data.dao.response

data class DeleteBookcaseResponse(
        val bookcaseId: Int
) {
    class Parser {

        fun parse(content: String): DeleteBookcaseResponse? {
            return DeleteBookcaseResponse(0)
        }
    }
}