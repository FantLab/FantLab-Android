package org.odddev.fantlab.core.models

import android.support.annotation.Keep

@Keep
data class LaResume(
		val authorId: Int,
		val resume: String,
		val position: Int
)