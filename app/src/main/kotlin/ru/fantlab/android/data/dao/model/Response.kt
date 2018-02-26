package ru.fantlab.android.data.dao.model

import android.os.Parcel
import android.os.Parcelable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.requery.*
import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.ResponseType.*
import ru.fantlab.android.helper.single
import timber.log.Timber
import java.util.*

@Entity @Table(name = "response")
data class Response(
		@get:Column(name = "id") @get:Key var responseId: Int,
		@get:Column(name = "date") var responseDate: Date,
		@get:Column(name = "text") var responseText: String,
		@get:Column(name = "votes") var responseVotes: Int,
		@get:Column var mark: Int?,
		@get:Column(name = "work_id") var workId: Int,
		@get:Column(name = "work_name") var workName: String,
		@get:Column(name = "work_name_orig") var workNameOrig: String,
		@get:Column(name = "work_type_id") var workTypeId: Int,
		@get:Column(name = "user_id") var userId: Int,
		@get:Column(name = "user_name") var userName: String,
		@get:Column(name = "user_sex") var userSex: String
) : Persistable, Parcelable {

	constructor(parcel: Parcel) : this(
			parcel.readInt(),
			Date(parcel.readLong()),
			parcel.readString(),
			parcel.readInt(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readInt(),
			parcel.readString(),
			parcel.readString(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readString(),
			parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(responseId)
		parcel.writeLong(responseDate.time)
		parcel.writeString(responseText)
		parcel.writeInt(responseVotes)
		parcel.writeValue(mark)
		parcel.writeInt(workId)
		parcel.writeString(workName)
		parcel.writeString(workNameOrig)
		parcel.writeInt(workTypeId)
		parcel.writeInt(userId)
		parcel.writeString(userName)
		parcel.writeString(userSex)
	}

	@Transient
	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Response> {
		override fun createFromParcel(parcel: Parcel): Response {
			return Response(parcel)
		}

		override fun newArray(size: Int): Array<Response?> {
			return arrayOfNulls(size)
		}
	}
}

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