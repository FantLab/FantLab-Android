package org.odddev.fantlab.core.utils

import com.google.gson.JsonElement
import java.util.*

fun JsonElement?.getField(): JsonElement? =
		if (this == null
				|| this.isJsonNull
				|| this.toString().replace("\"","").isEmpty())
			null
		else
			this

fun String.parseToDate(): Calendar = Calendar.getInstance().apply {
	set(Calendar.YEAR, this@parseToDate.substring(0..3).toInt())
	set(Calendar.MONTH, this@parseToDate.substring(5..6).toInt() - 1)
	set(Calendar.DAY_OF_MONTH, this@parseToDate.substring(8..9).toInt())
	set(Calendar.HOUR, 0)
	set(Calendar.MINUTE, 0)
	set(Calendar.SECOND, 0)
}
