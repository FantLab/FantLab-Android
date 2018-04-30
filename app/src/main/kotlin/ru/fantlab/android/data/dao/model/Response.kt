package ru.fantlab.android.data.dao.model

import android.os.Parcelable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.requery.*
import kotlinx.android.parcel.Parcelize
import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.ResponseType.*
import ru.fantlab.android.helper.single
import timber.log.Timber
import java.util.*

@Parcelize
@Entity @Table(name = "response")
data class Response(
		@get:Column(name = "id") @get:Key var responseId: Int,
		@get:Column(name = "date") var responseDate: Date,
		@get:Column(name = "text") var responseText: String,
		@get:Column(name = "votes") var responseVotes: Int,
		@get:Column var mark: Int?,
		@get:Column(name = "work_id") var workId: Int,
		@get:Column(name = "work_author") var workAuthor: String,
		@get:Column(name = "work_author_orig") var workAuthorOrig: String,
		@get:Column(name = "work_name") var workName: String,
		@get:Column(name = "work_name_orig") var workNameOrig: String,
		@get:Column(name = "work_type") var workType: String,
		@get:Column(name = "work_type_id") var workTypeId: Int,
		@get:Column(name = "work_image") var workImage: String,
		@get:Column(name = "user_id") var userId: Int,
		@get:Column(name = "user_name") var userName: String,
		@get:Column(name = "user_sex") var userSex: String,
		@get:Column(name = "user_avatar") var userAvatar: String
) : Persistable, Parcelable

fun List<Response>.save(): Disposable {
	return Single.fromPublisher<String> { s ->
		try {
			val dataSource = App.dataStore.toBlocking()
			if (!this.isEmpty()) {
				for (response in this) {
					dataSource.delete(Response::class.java)
							.where(RESPONSE_ID.eq(response.responseId))
							.get()
							.value()
					dataSource.insert(response)
				}
			}
			s.onNext("")
		} catch (e: Exception) {
			s.onError(e)
		}
		s.onComplete()
	}.single().subscribe({ }, Timber::e)
}

fun getUserResponses(userId: Int): Single<List<Response>> {
	return App.dataStore
			.select(Response::class.java)
			.where(USER_ID.eq(userId))
			.orderBy(RESPONSE_DATE.desc())
			.get()
			.observable()
			.toList()
			.single()
}

fun getAuthorResponses(authorId: Int): Single<List<Response>> {
	return App.dataStore
			.select(Response::class.java)
			// todo исправить после фикса API
			//.where(AUTHOR_ID.eq(authorId))
			.orderBy(RESPONSE_DATE.desc())
			.get()
			.observable()
			.toList()
			.single()
}

fun getWorkResponses(workId: Int, sort: String = "date"): Single<List<Response>> {
	val values = App.dataStore
			.select(Response::class.java)
			.where(WORK_ID.eq(workId))
	val orderedValues = when (sort) {
		"date" -> values.orderBy(RESPONSE_DATE.desc())
		"rating" -> values.orderBy(RESPONSE_VOTES.desc())
		"mark" -> values.orderBy(MARK.desc(), RESPONSE_VOTES.desc())
		else -> values.orderBy(RESPONSE_DATE.desc())
	}
	return orderedValues
			.get()
			.observable()
			.toList()
			.single()
}