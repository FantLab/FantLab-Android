package org.odddev.fantlab.core.models

import android.support.annotation.Keep

@Keep
data class Site (
		val authorId: Int,
		val description: String,
		val url: String,
		val position: Int
)