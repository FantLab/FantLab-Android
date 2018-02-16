package ru.fantlab.android.data.dao.model

import android.os.Parcel
import android.os.Parcelable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.requery.Column
import io.requery.Entity
import io.requery.Key
import ru.fantlab.android.App
import ru.fantlab.android.helper.single
import timber.log.Timber

// todo заменить на реальные поля
@Entity
abstract class AbstractResponse() : Parcelable {

	@JvmField
	@Column
	@Key
	var id: Int? = null

	constructor(parcel: Parcel) : this() {
		id = parcel.readValue(Int::class.java.classLoader) as? Int
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(id)
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

fun List<Response>?.save(): Disposable {
	return Single.fromPublisher<String> { s ->
		try {
			val dataSource = App.dataStore.toBlocking()
			if (this != null && !this.isEmpty()) {
				for (response in this) {
					dataSource.delete(Response::class.java)
							.where(Response.ID.eq(response.id))
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

fun getResponses(): Single<List<Response>> {
	return App.dataStore
			.select(Response::class.java)
			//.orderBy(Response.PUB_DATE.desc())
			.get()
			.observable()
			.toList()
			.single()
}
