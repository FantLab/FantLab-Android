package org.odddev.fantlab.core.models

import android.support.annotation.Keep

@Keep
data class AuthorAward(
		val id: Int,
		val inList: Boolean? = null,
		val isOpened: Boolean? = null,
		val name: String? = null,
		val rusName: String? = null,
		val contestId: Int? = null,
		val contestName: String? = null,
		val contestYear: Int? = null,
		val cwId: Int? = null,
		val cwIsWinner: Boolean? = null,
		val cwPostfix: String? = null,
		val cwPrefix: String? = null,
		val nominationId: Int? = null,
		val nominationName: String? = null,
		val nominationRusname: String? = null,
		val workId: Int? = null,
		val workName: String? = null,
		val workRusname: String? = null,
		val workYear: Int? = null
)