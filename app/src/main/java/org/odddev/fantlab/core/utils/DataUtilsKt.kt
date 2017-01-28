package org.odddev.fantlab.core.utils

import java.util.*

/**
 * Created by kefir on 28.01.2017.
 */

fun String.toDate(string: String): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, string.substring(0..3).toInt())
    return calendar
}