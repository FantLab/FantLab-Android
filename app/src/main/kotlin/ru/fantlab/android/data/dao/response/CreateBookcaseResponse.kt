package ru.fantlab.android.data.dao.response

data class CreateBookcaseResponse(
        val bookcaseId: Int?
) {
    class Parser {

        fun parse(content: String): CreateBookcaseResponse {
            return CreateBookcaseResponse(content.toIntOrNull())
        }
    }
}