package org.odddev.fantlab.core.utils

import android.text.TextUtils

import java.util.Calendar

/**
 * @author kenrube
 * *
 * @since 07.10.16
 */

object DateUtils {

	private val DELIMITER = "."
	private val SPLITTER = "\\."
	private val TWO_DIGITS_FORMAT = "%2s"
	private val SPACE = " "
	private val ZERO = "0"

	fun valuesToDateString(day: Int, month: Int, year: Int): String {
		val dayStr = String.format(TWO_DIGITS_FORMAT, day).replace(SPACE, ZERO)
		val monthStr = String.format(TWO_DIGITS_FORMAT, month).replace(SPACE, ZERO)
		val yearStr = year.toString()
		return TextUtils.join(DELIMITER, arrayOf(dayStr, monthStr, yearStr))
	}

	fun dateStringToCalendar(dateString: String): Calendar {
		val values = dateString.split(SPLITTER.toRegex()).dropLastWhile(String::isEmpty).toTypedArray()

		val calendar = Calendar.getInstance()
		calendar.set(
				Integer.parseInt(values[2]),
				Integer.parseInt(values[1]) - 1,
				Integer.parseInt(values[0]))

		return calendar
	}
}
