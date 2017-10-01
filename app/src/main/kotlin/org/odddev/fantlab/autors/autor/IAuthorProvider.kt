package org.odddev.fantlab.autors.autor

import io.reactivex.Observable
import org.odddev.fantlab.core.db.AuthorRecord

interface IAuthorProvider {

	fun getAuthor(id: Int): Observable<Iterable<AuthorRecord>>
}
