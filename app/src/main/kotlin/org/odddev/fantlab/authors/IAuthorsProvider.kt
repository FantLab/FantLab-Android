package org.odddev.fantlab.authors

import io.reactivex.Flowable
import org.odddev.fantlab.author.models.Author

interface IAuthorsProvider {

	fun getAuthors(): Flowable<List<AuthorInList>>
}
