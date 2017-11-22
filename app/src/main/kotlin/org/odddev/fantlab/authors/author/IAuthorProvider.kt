package org.odddev.fantlab.authors.author

import io.reactivex.Observable

interface IAuthorProvider {

	fun getAuthor(id: Int): Observable<Void>
}
