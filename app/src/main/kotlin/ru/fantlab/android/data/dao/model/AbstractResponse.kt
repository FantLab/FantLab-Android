package ru.fantlab.android.data.dao.model

import android.os.Parcel
import android.os.Parcelable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.requery.Column
import io.requery.Entity
import io.requery.Key
import io.requery.Table
import ru.fantlab.android.App
import ru.fantlab.android.data.dao.model.Response.RESPONSE_DATE
import ru.fantlab.android.data.dao.model.Response.USER_ID
import ru.fantlab.android.helper.single
import timber.log.Timber
import java.util.*

@Entity @Table(name = "response")
abstract class AbstractResponse() : Parcelable {

	@JvmField @Column(name = "id") @Key var responseId: Int? = null
	@JvmField @Column(name = "date") var responseDate: Date? = null
	@JvmField @Column(name = "text") var responseText: String? = null
	@JvmField @Column(name = "votes") var responseVotes: Int? = null
	@JvmField @Column var mark: Int? = null
	@JvmField @Column(name = "work_id") var workId: Int? = null
	@JvmField @Column(name = "work_name") var workName: String? = null
	@JvmField @Column(name = "work_name_orig") var workNameOrig: String? = null
	@JvmField @Column(name = "work_type_id") var workTypeId: Int? = null
	@JvmField @Column(name = "user_id") var userId: Int? = null
	@JvmField @Column(name = "user_name") var userName: String? = null
	@JvmField @Column(name = "user_sex") var userSex: String? = null

	constructor(parcel: Parcel) : this() {
		responseId = parcel.readValue(Int::class.java.classLoader) as? Int
		val tmpResponseDate = parcel.readLong()
		responseDate = if (tmpResponseDate == -1L) null else Date(tmpResponseDate)
		responseText = parcel.readString()
		responseVotes = parcel.readValue(Int::class.java.classLoader) as? Int
		mark = parcel.readValue(Int::class.java.classLoader) as? Int
		workId = parcel.readValue(Int::class.java.classLoader) as? Int
		workName = parcel.readString()
		workNameOrig = parcel.readString()
		workTypeId = parcel.readValue(Int::class.java.classLoader) as? Int
		userId = parcel.readValue(Int::class.java.classLoader) as? Int
		userName = parcel.readString()
		userSex = parcel.readString()
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(responseId)
		parcel.writeLong(if (responseDate != null) responseDate!!.time else -1)
		parcel.writeString(responseText)
		parcel.writeValue(responseVotes)
		parcel.writeValue(mark)
		parcel.writeValue(workId)
		parcel.writeString(workName)
		parcel.writeString(workNameOrig)
		parcel.writeValue(workTypeId)
		parcel.writeValue(userId)
		parcel.writeString(userName)
		parcel.writeString(userSex)
	}

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
							.where(Response.RESPONSE_ID.eq(response.responseId))
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
