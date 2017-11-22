package org.odddev.fantlab.authors

import android.support.annotation.Keep

@Keep
class AuthorsResponse(val list: List<Author>) {

	fun getAuthorsList(): List<Author> = list
}