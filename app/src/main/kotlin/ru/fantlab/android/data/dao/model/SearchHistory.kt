package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import io.reactivex.Single
import io.requery.Column
import io.requery.Entity
import io.requery.Persistable
import io.requery.Table
import kotlinx.android.parcel.Parcelize
import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.SearchHistoryType.TEXT
import ru.fantlab.android.helper.single

@Parcelize
@Entity @Table(name = "search_history")
data class SearchHistory(
		@get:Column(unique = true) var text: String
) : Persistable, Parcelable

fun SearchHistory.save(): Single<SearchHistory> {
	return App.dataStore
			.delete(SearchHistory::class.java)
			.where(TEXT.eq(this.text))
			.get()
			.single()
			.flatMap { App.dataStore.insert(this) }
			.single()
}

fun getSearchHistory(): Single<List<SearchHistory>> {
	return App.dataStore
			.select(SearchHistory::class.java)
			.groupBy(TEXT.asc())
			.get()
			.observable()
			.toList()
}

fun deleteSearchHistory() {
	App.dataStore
			.delete(SearchHistory::class.java)
			.get()
			.value()
}
