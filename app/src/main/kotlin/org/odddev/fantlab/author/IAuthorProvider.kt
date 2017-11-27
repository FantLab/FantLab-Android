package org.odddev.fantlab.author

import io.reactivex.Flowable
import org.odddev.fantlab.author.models.Author

interface IAuthorProvider {

	fun getAuthor(id: Int): Flowable<Author>
}
