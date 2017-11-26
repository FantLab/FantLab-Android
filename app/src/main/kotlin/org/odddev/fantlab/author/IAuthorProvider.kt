package org.odddev.fantlab.author

import io.reactivex.Flowable

interface IAuthorProvider {

	fun getAuthor(id: Int): Flowable<Void>
}
