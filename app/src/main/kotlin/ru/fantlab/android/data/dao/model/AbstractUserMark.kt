package ru.fantlab.android.data.dao.model

import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.requery.Column
import io.requery.Entity
import io.requery.Key
import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.Login.USER_ID
import ru.fantlab.android.helper.single

@Entity
abstract class AbstractUserMark {

	@JvmField
	@Column
	@Key
	var id: Int? = null

	@JvmField
	@Column
	var userId: Int? = null
}

fun List<UserMark>.save(userId: Int): Disposable {
	return Single.fromPublisher<String> { s ->
		try {
			val dataSource = App.dataStore.toBlocking()
			dataSource.delete(UserMark::class.java)
					.where(USER_ID.eq(userId))
					.get()
					.value()
			for (mark in this) {
				dataSource.delete(UserMark::class.java)
						.where(UserMark.ID.eq(mark.id))
						.get()
						.value()
				mark.userId = userId
				dataSource.insert(mark)
			}
			s.onNext("")
		} catch (e: Exception) {
			s.onError(e)
		}
		s.onComplete()
	}.single().subscribe({ /*donothing*/ }, Throwable::printStackTrace)
}

fun getUserMarks(userId: Int): Single<List<UserMark>> {
	return App.dataStore
			.select(UserMark::class.java)
			.where(USER_ID.eq(userId))
			//.orderBy(UPDATED_AT.desc())
			.get()
			.observable()
			.toList()
}