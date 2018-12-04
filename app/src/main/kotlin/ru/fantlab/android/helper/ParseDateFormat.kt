package ru.fantlab.android.helper

import android.os.Build.VERSION.SDK_INT
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

fun String.parseFullDate(isIso: Boolean = false): Date? {
	return try {
		val timezone = if (SDK_INT >= android.os.Build.VERSION_CODES.N) "X" else "Z"
		SimpleDateFormat(if (isIso) "yyyy-MM-dd'T'HH:mm:ss$timezone" else "yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("ru")).parse(this)
	} catch (e: Exception) {
		return null
	}
}

fun Date?.getTimeAgo(): CharSequence {
	this?.let {
		val now = System.currentTimeMillis()
		return DateUtils.getRelativeTimeSpanString(this.time, now, DateUtils.SECOND_IN_MILLIS)
	}
	return "N/A"
}
