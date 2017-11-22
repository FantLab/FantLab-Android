package org.odddev.fantlab.authors

import io.reactivex.Single

interface IAuthorsProvider {

	fun getAuthors(): Single<AuthorsResponse>
}
