package ru.fantlab.android.data.dao.model

import android.os.Parcel
import android.os.Parcelable
import io.reactivex.Single
import io.requery.Column
import io.requery.Entity
import io.requery.Table
import ru.fantlab.android.App
import ru.fantlab.android.helper.single

@Entity @Table(name = "search_history")
abstract class AbstractSearchHistory() : Parcelable {

	@JvmField @Column(unique = true) var text: String? = null

	constructor(parcel: Parcel) : this() {
		text = parcel.readString()
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(text)
	}

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
			.where(SearchHistory.TEXT.eq(this.text))
			.get()
			.single()
			.flatMap { App.dataStore.insert(this) }
			.single()
}

fun getSearchHistory(): Single<List<SearchHistory>> {
	return App.dataStore
			.select(SearchHistory::class.java)
			.groupBy(SearchHistory.TEXT.asc())
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