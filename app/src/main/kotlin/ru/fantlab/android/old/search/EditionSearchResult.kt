package ru.fantlab.android.old.search

data class EditionSearchResult(
		val authors: String,
		val comment: String,
		val compilers: String,
		val correct: Int,
		val editionId: Int,
		val isbn: String,
		val name: String,
		val notes: String,
		val planDate: String,
		val publisher: String,
		val series: String,
		val year: Int
)