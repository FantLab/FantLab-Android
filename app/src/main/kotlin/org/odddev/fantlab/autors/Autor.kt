package org.odddev.fantlab.autors

import android.support.annotation.Keep

@Keep
class Autor(
		val id: Int,
		val isFv: Boolean = false,
		val name: String,
		val nameOrig: String,
		val nameRp: String = "",
		val nameShort: String = "",
		val type: String,
		val isOpened: Boolean = true)
