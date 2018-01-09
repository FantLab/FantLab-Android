package ru.fantlab.android.search

data class SearchResult(
		val authorsSearchResult: AuthorsSearchResult,
		val worksSearchResult: WorksSearchResult,
		val editionsSearchResult: EditionsSearchResult
)