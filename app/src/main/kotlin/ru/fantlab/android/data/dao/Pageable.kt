package ru.fantlab.android.data.dao

data class Pageable<M>(
		val last: Int = 0,
		val totalCount: Int = 0,
		val items: ArrayList<M>
)