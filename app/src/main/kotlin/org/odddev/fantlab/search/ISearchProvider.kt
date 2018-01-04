package org.odddev.fantlab.search

import io.reactivex.Flowable

interface ISearchProvider {

	fun search(query: String): Flowable<SearchResult>
}