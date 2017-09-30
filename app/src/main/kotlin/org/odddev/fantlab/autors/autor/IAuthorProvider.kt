package org.odddev.fantlab.autors.autor

import io.reactivex.Observable

interface IAuthorProvider {

	fun getAuthor(id: Int): Observable<AuthorPageInfo>
}
