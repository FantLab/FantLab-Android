package ru.fantlab.android.old.core.models

import android.support.annotation.Keep

@Keep
data class Site (
		val authorId: Int,
		val description: String,
		val url: String,
		val position: Int
)