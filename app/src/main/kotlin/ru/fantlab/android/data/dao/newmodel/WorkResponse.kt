package ru.fantlab.android.data.dao.newmodel

data class WorkResponse(
		val work: Work,
		val awards: List<Nomination>?,
		val children: List<ChildWork>,
		val classificatory: List<GenreGroup>,
		val editions: EditionsBlocks
)