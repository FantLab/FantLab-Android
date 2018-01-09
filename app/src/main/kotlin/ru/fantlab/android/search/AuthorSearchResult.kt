package ru.fantlab.android.search

data class AuthorSearchResult(
		val authorId: Int,
		val birthYear: Int?,
		val country: String,
		val countryId: Int,
		val deathYear: Int?,
		val editionCount: Int,
		val isOpened: Boolean,
		val markCount: Int,
		val midMark: Int,
		val movieCount: Int,
		val name: String,
		val pseudoNames: String,
		val responseCount: Int,
		val rusName: String
)