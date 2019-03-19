package ru.fantlab.android.data.dao.response

data class CreateBookcaseResponse(
        val bookcaseId: Int
) {
    class Parser {

        fun parse(content: String): CreateBookcaseResponse? {
            var idValue: Int
            try {
                idValue = content.toInt()
            } catch (e: NumberFormatException) {
                return null
            }
            return CreateBookcaseResponse(idValue)
        }
    }
}