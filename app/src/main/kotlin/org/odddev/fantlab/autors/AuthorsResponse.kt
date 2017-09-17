package org.odddev.fantlab.autors

import android.support.annotation.Keep

@Keep
class AuthorsResponse(val list: List<Autor>) {

	fun getAuthorsList(): List<Autor> = list
}