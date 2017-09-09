package org.odddev.fantlab.autors

import android.support.annotation.Keep

@Keep
class AutorsResponse(val list: List<Autor>) {

	fun getAutorsList(): List<Autor> = list
}