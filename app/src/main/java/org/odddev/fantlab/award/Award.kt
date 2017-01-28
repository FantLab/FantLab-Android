package org.odddev.fantlab.award

import java.util.*

/**
 * Created by kefir on 28.01.2017.
 */
class Award(
        val awardId: String,
        val countryName: String,
        val description: String,
        val maxDate: Calendar,
        val minDate: Calendar,
        val name: String,
        val rusName: String) {


    fun getStartYear(): String {
        return minDate.get(Calendar.YEAR).toString()
    }

    fun getEndYear(): String {
        return maxDate.get(Calendar.YEAR).toString()
    }
}