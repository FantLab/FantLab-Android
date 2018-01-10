package ru.fantlab.android.old.search

data class SearchResult(
		val authorsSearchResult: AuthorsSearchResult,
		val worksSearchResult: WorksSearchResult,
		val editionsSearchResult: EditionsSearchResult
)