package ru.fantlab.android.data.dao

data class Pageable<M>(
		var next: Int = 0,
		var last: Int = 0,
		var totalCount: Int = 0,
		var incompleteResults: Boolean = false,
		var items: ArrayList<M>
)