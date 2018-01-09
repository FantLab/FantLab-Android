package ru.fantlab.android.core.models

import android.support.annotation.Keep

@Keep
data class Pseudonym(
		val authorId: Int,
		val isReal: Boolean,
		val name: String,
		val nameOrig: String,
		val position: Int
)