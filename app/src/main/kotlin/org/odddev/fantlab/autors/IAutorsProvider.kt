package org.odddev.fantlab.autors

import io.reactivex.Observable

interface IAutorsProvider {

	fun getAutors(): Observable<AuthorsResponse>
}
