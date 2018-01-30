package ru.fantlab.android.data.dao.model

import io.reactivex.Single
import io.requery.Column
import io.requery.Entity
import ru.fantlab.android.App
import ru.fantlab.android.helper.single

@Entity
abstract class AbstractSearchHistory {

	@JvmField
	@Column(unique = true)
	var text: String? = null

	fun save(entity: SearchHistory): Single<SearchHistory> {
		return App.dataStore
				.delete(SearchHistory::class.java)
				.where(SearchHistory.TEXT.eq(entity.text))
				.get()
				.single()
				.flatMap { App.dataStore.insert(entity) }
				.single()
	}

	companion object {

		fun getHistory(): Single<List<SearchHistory>> {
			return App.dataStore
					.select(SearchHistory::class.java)
					.groupBy(SearchHistory.TEXT.asc())
					.get()
					.observable()
					.toList()
		}

		fun deleteAll() {
			App.dataStore
					.delete(SearchHistory::class.java)
					.get()
					.value()
		}
	}
}