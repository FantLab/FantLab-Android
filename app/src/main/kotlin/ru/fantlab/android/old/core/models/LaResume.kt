package ru.fantlab.android.old.core.models

import android.support.annotation.Keep

@Keep
data class LaResume(
		val authorId: Int,
		val resume: String,
		val position: Int
)