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
import java.util.*

// todo заменить на реальные поля
@Entity
abstract class AbstractNews() : Parcelable {

	@JvmField
	@Column
	@Key
	var id: Int? = null

	@JvmField
	@Column
	var title: String? = null

	@JvmField
	@Column
	var description: String? = null

	@JvmField
	@Column
	var pubDate: Date? = null

	@JvmField
	@Column
	var author: String? = null

	constructor(parcel: Parcel) : this() {
		id = parcel.readValue(Int::class.java.classLoader) as? Int
		title = parcel.readString()
		description = parcel.readString()
		author = parcel.readString()
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(id)
		parcel.writeString(title)
		parcel.writeString(description)
		parcel.writeString(author)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<News> {
		override fun createFromParcel(parcel: Parcel): News {
			return News(parcel)
		}

		override fun newArray(size: Int): Array<News?> {
			return arrayOfNulls(size)
		}
	}
}

fun List<News>?.save(): Disposable {
	return Single.fromPublisher<String> { s ->
		try {
			val dataSource = App.dataStore.toBlocking()
			if (this != null && !this.isEmpty()) {
				for (newsEntry in this) {
					dataSource.delete(News::class.java)
							.where(News.ID.eq(newsEntry.id))
							.get()
							.value()
					dataSource.insert(newsEntry)
				}
			}
			s.onNext("")
		} catch (e: Exception) {
			s.onError(e)
		}
		s.onComplete()
	}.single().subscribe({ }, Timber::e)
}

fun getNews(): Single<List<News>> {
	return App.dataStore
			.select(News::class.java)
			//.orderBy(News.PUB_DATE.desc())
			.get()
			.observable()
			.toList()
			.single()
}