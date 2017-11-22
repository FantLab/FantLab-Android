package org.odddev.fantlab.author

import io.reactivex.Observable

interface IAuthorProvider {

	fun getAuthor(id: Int): Observable<Void>
}
