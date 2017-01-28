package org.odddev.fantlab.core.utils

import java.util.*

/**
 * Created by kefir on 28.01.2017.
 */

fun String.parseToYear(): Calendar {
    return Calendar.getInstance().apply {
        set(Calendar.YEAR, this@parseToYear.substring(0..3).toInt())
        set(Calendar.MONTH, this@parseToYear.substring(4..5).toInt() + 1)
        set(Calendar.DAY_OF_MONTH, this@parseToYear.substring(8..9).toInt() + 1)
    }
}