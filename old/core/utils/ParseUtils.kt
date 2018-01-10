package ru.fantlab.android.old.core.utils

import java.util.*

fun String.parseToDate(): Calendar = Calendar.getInstance().apply {
	set(Calendar.YEAR, this@parseToDate.substring(0..3).toInt())
	set(Calendar.MONTH, this@parseToDate.substring(5..6).toInt() - 1)
	set(Calendar.DAY_OF_MONTH, this@parseToDate.substring(8..9).toInt())
	set(Calendar.HOUR, 0)
	set(Calendar.MINUTE, 0)
	set(Calendar.SECOND, 0)
}
