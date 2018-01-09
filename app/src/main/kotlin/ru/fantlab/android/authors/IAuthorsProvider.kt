package ru.fantlab.android.authors

import io.reactivex.Flowable

interface IAuthorsProvider {

	fun getAuthors(): Flowable<List<AuthorInList>>
}
