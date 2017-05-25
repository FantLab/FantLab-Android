package org.odddev.fantlab.award

import java.util.*

/**
 * Created by kefir on 28.01.2017.
 */
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