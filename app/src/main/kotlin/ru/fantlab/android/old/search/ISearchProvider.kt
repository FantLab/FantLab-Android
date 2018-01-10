package ru.fantlab.android.old.search

import io.reactivex.Flowable

interface ISearchProvider {

	fun search(query: String): Flowable<SearchResult>
}