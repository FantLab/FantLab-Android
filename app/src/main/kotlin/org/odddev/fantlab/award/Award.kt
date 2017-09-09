package org.odddev.fantlab.award

import android.support.annotation.Keep
import java.util.*

@Keep
class Award(
		val awardId: String,
		val country: String,
		val description: String,
		val maxDate: Calendar,
		val minDate: Calendar,
		val name: String,
		val rusName: String,
		val language: Int,
		val type: Int) {

	fun getStartYear(): String = minDate.get(Calendar.YEAR).toString()

	fun getEndYear(): String = maxDate.get(Calendar.YEAR).toString()
}