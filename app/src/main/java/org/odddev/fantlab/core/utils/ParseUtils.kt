package org.odddev.fantlab.core.utils

import java.util.*

/**
 * Created by kefir on 28.01.2017.
 */

fun String.parseToDate(): Calendar {
    return Calendar.getInstance().apply {
        set(Calendar.YEAR, this@parseToDate.substring(0..3).toInt())
        set(Calendar.MONTH, this@parseToDate.substring(4..5).toInt() + 1)
        set(Calendar.DAY_OF_MONTH, this@parseToDate.substring(8..9).toInt() + 1)
    }
}