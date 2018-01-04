package org.odddev.fantlab.search

data class AuthorsSearchResult(
		val authorsSearchResult: List<AuthorSearchResult>,
		val total: Int
)