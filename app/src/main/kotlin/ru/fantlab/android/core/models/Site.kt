package ru.fantlab.android.core.models

import android.support.annotation.Keep

@Keep
data class Site (
		val authorId: Int,
		val description: String,
		val url: String,
		val position: Int
)