package ru.fantlab.android.helper

import android.text.format.DateUtils
import java.util.*

class ParseDateFormat

fun Date?.getTimeAgo(): CharSequence {
	this?.let {
		val now = System.currentTimeMillis()
		return DateUtils.getRelativeTimeSpanString(this.time, now, DateUtils.SECOND_IN_MILLIS)
	}
	return "N/A"
}
