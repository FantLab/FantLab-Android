package ru.fantlab.android.data.dao.response

class UpdateBookcaseResponse (
        val resCode: Int
) {
    class Parser {

        fun parse(content: String): UpdateBookcaseResponse? {
            var res: Int
            try {
                res = content.toInt()
            } catch (e: NumberFormatException) {
                return null
            }
            return UpdateBookcaseResponse(res)
        }
    }
}