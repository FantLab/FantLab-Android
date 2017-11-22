package org.odddev.fantlab.authors

import io.reactivex.Observable

interface IAuthorsProvider {

	fun getAuthors(): Observable<AuthorsResponse>
}
