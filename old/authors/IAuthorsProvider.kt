package ru.fantlab.android.old.authors

import io.reactivex.Flowable

interface IAuthorsProvider {

	fun getAuthors(): Flowable<List<AuthorInList>>
}
