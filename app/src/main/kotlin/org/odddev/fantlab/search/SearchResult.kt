package org.odddev.fantlab.search

data class SearchResult(
		val authorsSearchResult: AuthorsSearchResult,
		val worksSearchResult: WorksSearchResult,
		val editionsSearchResult: EditionsSearchResult
)