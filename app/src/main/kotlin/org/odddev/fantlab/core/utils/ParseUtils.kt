package org.odddev.fantlab.core.utils

import com.google.gson.JsonElement
import java.util.*

/**
 * Created by kefir on 28.01.2017.
 */

fun String.parseToDate(): Calendar {
	return Calendar.getInstance().apply {
		set(Calendar.YEAR, this@parseToDate.substring(0..3).toInt())
		set(Calendar.MONTH, this@parseToDate.substring(5..6).toInt() - 1)
		set(Calendar.DAY_OF_MONTH, this@parseToDate.substring(8..9).toInt())
		set(Calendar.HOUR, 0)
		set(Calendar.MINUTE, 0)
		set(Calendar.SECOND, 0)
	}
}

fun JsonElement?.getField(): JsonElement? {
	return if (this == null || this.isJsonNull || this.asString.isEmpty()) null else this
}
