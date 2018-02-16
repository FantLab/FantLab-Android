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
abstract class AbstractForumMessage() : Parcelable {

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

	companion object CREATOR : Parcelable.Creator<ForumMessage> {
		override fun createFromParcel(parcel: Parcel): ForumMessage {
			return ForumMessage(parcel)
		}

		override fun newArray(size: Int): Array<ForumMessage?> {
			return arrayOfNulls(size)
		}
	}
}

fun List<ForumMessage>?.save(): Disposable {
	return Single.fromPublisher<String> { s ->
		try {
			val dataSource = App.dataStore.toBlocking()
			if (this != null && !this.isEmpty()) {
				for (message in this) {
					dataSource.delete(ForumMessage::class.java)
							.where(ForumMessage.ID.eq(message.id))
							.get()
							.value()
					dataSource.insert(message)
				}
			}
			s.onNext("")
		} catch (e: Exception) {
			s.onError(e)
		}
		s.onComplete()
	}.single().subscribe({ }, Timber::e)
}

fun getForumMessages(): Single<List<ForumMessage>> {
	return App.dataStore
			.select(ForumMessage::class.java)
			//.orderBy(ForumMessage.PUB_DATE.desc())
			.get()
			.observable()
			.toList()
			.single()
}