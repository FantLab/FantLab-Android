package ru.fantlab.android.data.dao.response

class UpdateBookcaseResponse (
        val resCode: Int?
) {
    class Parser {

        fun parse(content: String): UpdateBookcaseResponse {
            return UpdateBookcaseResponse(content.toIntOrNull())
        }
    }
}