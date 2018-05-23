package ru.fantlab.android.helper

import android.net.ParseException
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

fun String.parseFullDate(): Date? {
	return try {
		SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("ru")).parse(this)
	} catch (e: ParseException) {
		null
	}
}

fun Date?.getTimeAgo(): CharSequence {
	this?.let {
		val now = System.currentTimeMillis()
		return DateUtils.getRelativeTimeSpanString(this.time, now, DateUtils.SECOND_IN_MILLIS)
	}
	return "N/A"
}
