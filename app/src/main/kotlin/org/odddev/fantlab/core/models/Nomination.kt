package org.odddev.fantlab.core.models

import android.support.annotation.Keep

@Keep
data class Nomination(
		// award_id
		val awardId: Int,
		// contest_id
		val contestId: Int? = null,
		// contest_name
		val contestName: String? = null,
		// contest_year
		val contestYear: Int? = null,
		// cw_id
		val cwId: Int? = null,
		// cw_is_winner
		val cwIsWinner: Boolean? = null,
		// cw_postfix
		val cwPostfix: String? = null,
		// cw_prefix
		val cwPrefix: String? = null,
		// award_in_list
		val inList: Boolean? = null,
		// award_is_opened
		val isOpened: Boolean? = null,
		// award_name
		val name: String? = null,
		// nomination_id
		val nominationId: Int? = null,
		// nomination_name
		val nominationName: String? = null,
		// nomination_rusname
		val nominationRusName: String? = null,
		// award_rusname
		val rusName: String? = null,
		// work_id
		val workId: Int? = null,
		// work_name
		val workName: String? = null,
		// work_rusname
		val workRusName: String? = null,
		// work_year
		val workYear: Int? = null
)
