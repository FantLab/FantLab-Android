package org.odddev.fantlab.award

import org.odddev.fantlab.core.utils.toDate
import java.util.*

/**
 * Created by kefir on 28.01.2017.
 */
class AwardDto {

    val id: String
    val countryName: String
    val description: String
    val minDate: Calendar
    val maxDate: Calendar
    val name: String
    val rusName: String

    constructor(awardRes: AwardRes) {
        this.id = awardRes.awardId
        this.countryName = awardRes.countryName
        this.description = awardRes.description ?: ""
        this.name = awardRes.name
        this.rusName = awardRes.rusname

        this.minDate = awardRes.minDate.toDate(awardRes.minDate)
        this.maxDate = awardRes.maxDate.toDate(awardRes.maxDate)
    }

    fun getStartYear(): String = minDate.get(Calendar.YEAR).toString()

    fun getEndYear(): String = maxDate.get(Calendar.YEAR).toString()
}