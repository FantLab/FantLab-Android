package ru.fantlab.android.data.dao.model

import android.os.Parcel
import android.os.Parcelable
import io.reactivex.Single
import io.requery.*
import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.SearchHistoryType.TEXT
import ru.fantlab.android.helper.single

@Entity @Table(name = "search_history")
data class SearchHistory(
		@get:Column(unique = true) var text: String
) : Persistable, Parcelable {

	constructor(parcel: Parcel) : this(parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(text)
	}

	@Transient
	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<SearchHistory> {
		override fun createFromParcel(parcel: Parcel): SearchHistory {
			return SearchHistory(parcel)
		}

		override fun newArray(size: Int): Array<SearchHistory?> {
			return arrayOfNulls(size)
		}
	}
}

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
